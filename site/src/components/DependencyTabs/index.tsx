import React from 'react';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import CodeBlock from '@theme/CodeBlock';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';

interface DependencyTabsProps {
  java?: string;
  kotlin?: string;
  scala?: string;
}

export default function DependencyTabs({ java, kotlin, scala }: DependencyTabsProps): JSX.Element {
  const { siteConfig } = useDocusaurusContext();
  const version = siteConfig.customFields.jdbcVersion as string;

  const items = [
    java && { value: 'java', label: 'Java', artifactId: java },
    kotlin && { value: 'kotlin', label: 'Kotlin', artifactId: kotlin },
    scala && { value: 'scala', label: 'Scala', artifactId: scala },
  ].filter(Boolean);

  return (
    <Tabs groupId="lang">
      {items.map(({ value, label, artifactId }) => (
        <TabItem key={value} value={value} label={label}>
          <CodeBlock language="kotlin" title="build.gradle.kts">
            {`implementation("dev.typr.foundations:${artifactId}:${version}")`}
          </CodeBlock>
          <CodeBlock language="xml" title="pom.xml">
            {`<dependency>
    <groupId>dev.typr.foundations</groupId>
    <artifactId>${artifactId}</artifactId>
    <version>${version}</version>
</dependency>`}
          </CodeBlock>
        </TabItem>
      ))}
    </Tabs>
  );
}
