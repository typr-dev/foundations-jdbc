import React, { useState, useMemo } from 'react';
import CodeBlock from '@theme/CodeBlock';
import styles from './styles.module.css';

// Import snippets data at build time
import snippetsData from '@site/static/snippets/snippets.json';

interface FileData {
  name: string;
  snippet: string;
  fullContent: string;
}

const allSnippets: Record<string, FileData> = snippetsData as Record<string, FileData>;

interface CodeSnippetProps {
  file: string;
  language: 'java' | 'kotlin' | 'scala';
}

export default function CodeSnippet({ file, language }: CodeSnippetProps): JSX.Element {
  const [showFullFile, setShowFullFile] = useState(false);

  const fileData = allSnippets[file];

  if (!fileData) {
    throw new Error(`File not found: "${file}". Available files: ${Object.keys(allSnippets).slice(0, 5).join(', ')}...`);
  }

  const displayCode = useMemo(() => {
    if (showFullFile) {
      return fileData.fullContent;
    }
    return fileData.snippet;
  }, [showFullFile, fileData.snippet, fileData.fullContent]);

  return (
    <div className={styles.codeSnippetContainer}>
      <div className={styles.controls}>
        <label className={styles.checkbox}>
          <input
            type="checkbox"
            checked={showFullFile}
            onChange={(e) => setShowFullFile(e.target.checked)}
          />
          <span>Show entire file</span>
        </label>
      </div>
      <CodeBlock language={language}>
        {displayCode}
      </CodeBlock>
    </div>
  );
}
