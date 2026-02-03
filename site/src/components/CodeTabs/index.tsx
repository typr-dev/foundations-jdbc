import React, { useState, useMemo } from 'react';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
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

interface CodeTabsProps {
  javaFile?: string;
  kotlinFile?: string;
  scalaFile?: string;
}

interface TabConfig {
  value: string;
  label: string;
  language: string;
  file: string;
}

function getSnippetCode(file: string, showFull: boolean): string {
  const fileData = allSnippets[file];

  if (!fileData) {
    throw new Error(`File not found: "${file}". Available files: ${Object.keys(allSnippets).slice(0, 5).join(', ')}...`);
  }

  if (showFull) {
    return fileData.fullContent;
  }

  return fileData.snippet;
}

export default function CodeTabs({
  javaFile,
  kotlinFile,
  scalaFile,
}: CodeTabsProps): JSX.Element {
  const [showFullFile, setShowFullFile] = useState(false);

  const tabs = useMemo(() => {
    const result: TabConfig[] = [];
    if (javaFile) {
      result.push({ value: 'java', label: 'Java', language: 'java', file: javaFile });
    }
    if (kotlinFile) {
      result.push({ value: 'kotlin', label: 'Kotlin', language: 'kotlin', file: kotlinFile });
    }
    if (scalaFile) {
      result.push({ value: 'scala', label: 'Scala', language: 'scala', file: scalaFile });
    }
    return result;
  }, [javaFile, kotlinFile, scalaFile]);

  if (tabs.length === 0) {
    throw new Error('CodeTabs requires at least one of: javaFile, kotlinFile, scalaFile');
  }

  return (
    <div className={styles.codeTabsContainer}>
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
      <Tabs groupId="language">
        {tabs.map(tab => (
          <TabItem key={tab.value} value={tab.value} label={tab.label}>
            <CodeBlock language={tab.language}>
              {getSnippetCode(tab.file, showFullFile)}
            </CodeBlock>
          </TabItem>
        ))}
      </Tabs>
    </div>
  );
}
