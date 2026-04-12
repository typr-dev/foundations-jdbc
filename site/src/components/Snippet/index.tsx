import React, { useState } from 'react';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import CodeBlock from '@theme/CodeBlock';
import styles from './styles.module.css';

import snippetsData from '@site/static/snippets/snippets.json';

interface FileData {
  name: string;
  snippet: string;
  fullContent: string;
}

const allSnippets: Record<string, FileData> = snippetsData as Record<string, FileData>;

type Lang = 'java' | 'kotlin' | 'scala';

interface SnippetProps {
  file: string;
  lang?: Lang;
  hideFullFile?: boolean;
}

function buildPath(file: string, lang: Lang): string {
  const ext = lang === 'java' ? '.java' : lang === 'kotlin' ? '.kt' : '.scala';
  const pkg = lang === 'java' ? 'foundations' : lang === 'kotlin' ? 'foundationskt' : 'foundationssc';
  return `documentation-examples-${lang}/src/${lang}/dev/typr/${pkg}/docs/${file}${ext}`;
}

function getSnippetCode(path: string, showFull: boolean): string {
  const fileData = allSnippets[path];

  if (!fileData) {
    throw new Error(
      `File not found: "${path}".\n\n` +
      `Make sure the file exists and has //start and //stop markers.\n` +
      `Available files: ${Object.keys(allSnippets).slice(0, 5).join(', ')}...`
    );
  }

  return showFull ? fileData.fullContent : fileData.snippet;
}

export default function Snippet({ file, lang, hideFullFile }: SnippetProps): JSX.Element {
  const [showFullFile, setShowFullFile] = useState(false);

  const langConfig: Record<Lang, string> = {
    java: 'java', kotlin: 'kotlin', scala: 'scala'
  };

  const langs: Lang[] = lang ? [lang] : ['kotlin', 'java', 'scala'];

  const codeBlocks = langs.map(l => ({
    lang: l,
    path: buildPath(file, l),
    syntax: langConfig[l],
  }));

  const content = (
    <>
      {langs.length === 1 ? (
        <CodeBlock language={codeBlocks[0].syntax}>
          {getSnippetCode(codeBlocks[0].path, showFullFile)}
        </CodeBlock>
      ) : (
        <Tabs groupId="language">
          {codeBlocks.map(({ lang: l, path, syntax }) => (
            <TabItem key={l} value={l} label={l.charAt(0).toUpperCase() + l.slice(1)}>
              <CodeBlock language={syntax}>
                {getSnippetCode(path, showFullFile)}
              </CodeBlock>
            </TabItem>
          ))}
        </Tabs>
      )}
    </>
  );

  return (
    <div className={styles.snippetContainer}>
      <div className={styles.tabsWrapper}>
        {content}
        {!hideFullFile && (
          <label className={styles.checkbox}>
            <input
              type="checkbox"
              checked={showFullFile}
              onChange={(e) => setShowFullFile(e.target.checked)}
            />
            <span>Show entire file</span>
          </label>
        )}
      </div>
    </div>
  );
}
