#!/usr/bin/env node

/**
 * Extracts code snippets from documentation example files.
 *
 * Each file should contain ONE snippet marked with:
 *   //start
 *   ... code ...
 *   //stop
 *
 * The snippet name is derived from the file name.
 * When "show entire file" is enabled, the full file is shown including imports.
 *
 * Outputs JSON files to static/snippets/ that the CodeSnippet component can load.
 */

const fs = require('fs');
const path = require('path');
const { glob } = require('glob');

const ROOT_DIR = path.resolve(__dirname, '../..');
const OUTPUT_DIR = path.resolve(__dirname, '../static/snippets');

function dedent(code) {
  const lines = code.split('\n');

  // Find minimum indentation (ignoring empty lines)
  let minIndent = Infinity;
  for (const line of lines) {
    if (line.trim().length === 0) continue;
    const indent = line.match(/^(\s*)/)[1].length;
    minIndent = Math.min(minIndent, indent);
  }

  if (minIndent === Infinity) minIndent = 0;

  // Remove that much indentation from all lines
  return lines
    .map(line => line.slice(minIndent))
    .join('\n')
    .trim();
}

function stripMarkerLines(content) {
  // Remove lines containing //start or //stop markers
  return content
    .split('\n')
    .filter(line => !line.match(/\/\/\s*start\b/) && !line.match(/\/\/\s*stop\b/))
    .join('\n');
}

function extractSnippet(content, filePath) {
  // Look for //start and //stop markers
  const startRegex = /\/\/\s*start\b/;
  const stopRegex = /\/\/\s*stop\b/;

  const startMatch = content.match(startRegex);
  const stopMatch = content.match(stopRegex);

  if (startMatch && !stopMatch) {
    throw new Error(`File "${filePath}" has //start but no matching //stop`);
  }

  if (stopMatch && !startMatch) {
    throw new Error(`File "${filePath}" has //stop but no matching //start`);
  }

  if (!startMatch) {
    // No markers - the entire file content (minus package/imports header) is the snippet
    return {
      snippet: extractContentWithoutHeader(content),
      fullContent: content.trim(),
      hasMarkers: false,
    };
  }

  // Extract content between //start and //stop
  const startIndex = startMatch.index + startMatch[0].length;
  const stopIndex = content.indexOf(stopMatch[0]);

  const snippetContent = content.slice(startIndex, stopIndex);

  return {
    snippet: dedent(snippetContent),
    fullContent: stripMarkerLines(content).trim(),
    hasMarkers: true,
  };
}

function extractContentWithoutHeader(content) {
  const lines = content.split('\n');
  let contentStartIndex = 0;

  // Skip past package declaration and imports
  for (let i = 0; i < lines.length; i++) {
    const trimmed = lines[i].trim();
    if (trimmed.startsWith('package ') ||
        trimmed.startsWith('import ') ||
        trimmed === '') {
      contentStartIndex = i + 1;
    } else {
      break;
    }
  }

  return dedent(lines.slice(contentStartIndex).join('\n'));
}

function getSnippetNameFromPath(filePath) {
  // Convert file path to snippet name
  // e.g., documentation-examples-java/src/java/dev/typr/foundations/docs/core/RowParserBasic.java
  // becomes: java/core/RowParserBasic

  const relativePath = path.relative(ROOT_DIR, filePath);

  // Extract language from module name
  let language;
  if (relativePath.startsWith('documentation-examples-java')) {
    language = 'java';
  } else if (relativePath.startsWith('documentation-examples-kotlin')) {
    language = 'kotlin';
  } else if (relativePath.startsWith('documentation-examples-scala')) {
    language = 'scala';
  } else {
    throw new Error(`Unknown module: ${relativePath}`);
  }

  // Extract the category and filename
  const match = relativePath.match(/docs\/([^/]+)\/([^.]+)/);
  if (!match) {
    throw new Error(`Could not parse path: ${relativePath}`);
  }

  const category = match[1];
  const name = match[2];

  return `${language}/${category}/${name}`;
}

async function processFile(filePath) {
  const content = fs.readFileSync(filePath, 'utf8');
  const relativePath = path.relative(ROOT_DIR, filePath);

  const { snippet, fullContent, hasMarkers } = extractSnippet(content, relativePath);
  const snippetName = getSnippetNameFromPath(filePath);

  return {
    name: snippetName,
    path: relativePath,
    snippet,
    fullContent,
    hasMarkers,
  };
}

async function main() {
  console.log('Extracting code snippets...');

  // Find all example files (only in docs/ folders)
  const patterns = [
    'documentation-examples-java/src/java/dev/typr/foundations/docs/**/*.java',
    'documentation-examples-kotlin/src/kotlin/dev/typr/foundations/docs/**/*.kt',
    'documentation-examples-scala/src/scala/dev/typr/foundations/docs/**/*.scala',
  ];

  const allFiles = [];
  for (const pattern of patterns) {
    const files = await glob(pattern, { cwd: ROOT_DIR });
    allFiles.push(...files.map(f => path.join(ROOT_DIR, f)));
  }

  if (allFiles.length === 0) {
    console.log('No example files found. Creating empty snippets file.');
    fs.mkdirSync(OUTPUT_DIR, { recursive: true });
    fs.writeFileSync(path.join(OUTPUT_DIR, 'snippets.json'), '{}');
    return;
  }

  console.log(`Found ${allFiles.length} example files`);

  // Process all files
  const allSnippets = {};
  const errors = [];

  for (const file of allFiles) {
    try {
      const result = await processFile(file);
      allSnippets[result.path] = {
        name: result.name,
        snippet: result.snippet,
        fullContent: result.fullContent,
      };
      console.log(`  ${result.name}: ${result.hasMarkers ? 'has markers' : 'no markers'}`);
    } catch (err) {
      errors.push(err.message);
    }
  }

  if (errors.length > 0) {
    console.error('\nErrors found:');
    for (const error of errors) {
      console.error(`  - ${error}`);
    }
    process.exit(1);
  }

  // Create output directory
  fs.mkdirSync(OUTPUT_DIR, { recursive: true });

  // Write combined output
  const outputPath = path.join(OUTPUT_DIR, 'snippets.json');
  fs.writeFileSync(outputPath, JSON.stringify(allSnippets, null, 2));

  console.log(`\nExtracted ${Object.keys(allSnippets).length} files to ${path.relative(ROOT_DIR, outputPath)}`);
}

main().catch(err => {
  console.error(err);
  process.exit(1);
});
