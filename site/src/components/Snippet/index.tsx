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

interface SnippetProps {
  file: string;
}

function buildPath(file: string, lang: 'java' | 'kotlin' | 'scala'): string {
  const ext = lang === 'java' ? '.java' : lang === 'kotlin' ? '.kt' : '.scala';
  return `documentation-examples-${lang}/src/${lang}/dev/typr/foundations/docs/${file}${ext}`;
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

export default function Snippet({ file }: SnippetProps): JSX.Element {
  const [showFullFile, setShowFullFile] = useState(false);

  const javaPath = buildPath(file, 'java');
  const kotlinPath = buildPath(file, 'kotlin');
  const scalaPath = buildPath(file, 'scala');

  return (
    <div className={styles.snippetContainer}>
      <div className={styles.tabsWrapper}>
        <Tabs groupId="language">
          <TabItem value="kotlin" label="Kotlin">
            <CodeBlock language="kotlin">
              {getSnippetCode(kotlinPath, showFullFile)}
            </CodeBlock>
          </TabItem>
          <TabItem value="java" label="Java">
            <CodeBlock language="java">
              {getSnippetCode(javaPath, showFullFile)}
            </CodeBlock>
          </TabItem>
          <TabItem value="scala" label="Scala">
            <CodeBlock language="scala">
              {getSnippetCode(scalaPath, showFullFile)}
            </CodeBlock>
          </TabItem>
        </Tabs>
        <label className={styles.checkbox}>
          <input
            type="checkbox"
            checked={showFullFile}
            onChange={(e) => setShowFullFile(e.target.checked)}
          />
          <span>Show entire file</span>
        </label>
      </div>
    </div>
  );
}
