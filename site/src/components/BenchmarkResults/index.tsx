import React, { useEffect, useState } from 'react';
import BrowserOnly from '@docusaurus/BrowserOnly';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import styles from './styles.module.css';

interface Result {
  name: string;
  library: string;
  opsPerSec: number;
  p50us: number;
  p95us: number;
  p99us: number;
  p999us: number;
  wallTimeMs: number;
}

interface Section {
  title: string;
  description: string;
  metricLabel: string;
  code?: string;
  results: Result[];
}

interface Report {
  title: string;
  timestamp: string;
  sections: Section[];
}

const LIBRARY_COLORS: Record<string, string> = {
  'Raw JDBC':             '#4b5563',
  'Foundations+Hikari':   '#2563eb',
  'Foundations+PgPipe':   '#059669',
  'Vert.x':              '#7c3aed',
  'Hibernate':           '#d97706',
};

function resolveLibrary(r: Result): string {
  if (r.library) return r.library;
  const n = r.name;
  if (n.startsWith('Foundations+PgPipe') || n.startsWith('Fnd+PgPipe') || n.startsWith('PgPipe') || n.startsWith('PgPipeline')) return 'Foundations+PgPipe';
  if (n.startsWith('Foundations+Hikari') || n.startsWith('Fnd+Hikari')) return 'Foundations+Hikari';
  if (n.startsWith('Raw JDBC') || n.startsWith('JDBC')) return 'Raw JDBC';
  if (n.startsWith('Hibernate')) return 'Hibernate';
  if (n.startsWith('Vert.x')) return 'Vert.x';
  return n;
}

function getColor(r: Result): string {
  return LIBRARY_COLORS[resolveLibrary(r)] || '#94a3b8';
}

function formatDuration(us: number): string {
  if (us >= 1_000_000) return `${(us / 1_000_000).toFixed(1)}s`;
  if (us >= 1_000) return `${(us / 1_000).toFixed(0)}ms`;
  return `${us}\u00B5s`;
}

function formatOps(val_: number): string {
  if (val_ >= 1_000_000) return `${(val_ / 1_000_000).toFixed(1)}M`;
  if (val_ >= 1_000) return `${(val_ / 1_000).toFixed(1)}k`;
  return val_.toFixed(0);
}

const PERCENTILES = ['p50', 'p95', 'p99', 'p99.9'] as const;

function buildLatencyData(results: Result[]): Array<Record<string, any>> {
  return PERCENTILES.map((p) => {
    const row: Record<string, any> = { percentile: p };
    for (const r of results) {
      const key = p === 'p50' ? 'p50us' : p === 'p95' ? 'p95us' : p === 'p99' ? 'p99us' : 'p999us';
      row[r.name] = r[key];
    }
    return row;
  });
}

const REPORT_FILES = [
  'throughput',
  'pipelining-advantage',
  'transactions',
  'readonly-latency',
  'batch-insert',
  'streaming',
  'update',
  'large-row',
  'cache-churn',
  'tx-latency',
];

const REPORT_INTROS: Record<string, string> = {
  'Throughput Benchmark': 'How fast can each contender serve concurrent point reads, inserts, and mixed workloads? Tests with 50 connections on localhost, plus scarce-connection scenarios with only 5.',
  'Pipelining Advantage Benchmark': 'The pipelining advantage under realistic 10ms network latency. Shows throughput, combine() fan-out, multi-statement transactions, and extreme connection scarcity (2 connections).',
  'Transaction Benchmark': 'Transaction overhead: how much does BEGIN/COMMIT cost? Compares non-transactional reads, single-read transactions, multi-operation transactions, and the readonly vs mutable execution paths.',
  'Readonly Latency Benchmark': 'The readonly + combine() path vs sequential and mutable paths under 10ms RTT. Shows why readonlyTransact + combine is the optimal pattern for independent reads.',
  'Batch Insert Benchmark': 'Bulk insert throughput: COPY protocol vs JDBC executeBatch vs Hibernate batch persist. Tests with different batch sizes and concurrency levels.',
  'Streaming Cursor Benchmark': 'Server-side cursor streaming with PgPipe\'s cursor prefetch vs standard JDBC and Vert.x. Tests different fetch sizes, concurrency levels, and network latencies.',
  'UPDATE Benchmark': 'UPDATE throughput on localhost, under 10ms latency, and with scarce connections.',
  'Large Row Benchmark': 'Point reads with wide rows (20 columns) — tests whether pipelining advantage holds when row parsing overhead increases.',
  'Cache Churn Benchmark': 'Prepared statement cache effectiveness with different working set sizes (8 to 1024 distinct queries). Tests whether cache eviction degrades throughput.',
  'Transaction Latency Benchmark': 'Sequential (non-concurrent) transaction latency: how long does a single BEGIN + SELECT + COMMIT take? Measures per-operation latency, not throughput.',
};

export default function BenchmarkResults(): React.JSX.Element {
  return (
    <BrowserOnly fallback={<p>Loading benchmark results...</p>}>
      {() => <BenchmarkResultsInner />}
    </BrowserOnly>
  );
}

function BenchmarkResultsInner(): React.JSX.Element {
  const [reports, setReports] = useState<Report[]>([]);
  const [loading, setLoading] = useState(true);
  const { siteConfig } = useDocusaurusContext();
  const baseUrl = siteConfig.baseUrl;

  useEffect(() => {
    const loadAll = async () => {
      const loaded: Report[] = [];
      for (const name of REPORT_FILES) {
        try {
          const url = `${baseUrl}benchmark-results/${name}.json`;
          const resp = await fetch(url);
          const ct = resp.headers.get('content-type') || '';
          if (resp.ok && ct.includes('json')) {
            const data = await resp.json();
            if (data.sections?.length > 0) {
              loaded.push(data);
            }
          }
        } catch (_) {}
      }
      setReports(loaded);
      setLoading(false);
    };
    loadAll();
  }, [baseUrl]);

  if (loading) {
    return <p>Loading benchmark results...</p>;
  }

  if (reports.length === 0) {
    return (
      <div className="admonition admonition-info alert alert--info">
        <div className="admonition-content">
          <p>No benchmark results found. Run <code>bleep run benchmark</code> to generate.</p>
        </div>
      </div>
    );
  }

  const {
    LineChart, Line, BarChart, Bar,
    XAxis, YAxis, Tooltip, ResponsiveContainer, Cell, Legend,
  } = require('recharts');

  return (
    <>
      {reports.map((report, ri) => (
        <div key={ri} className={styles.report}>
          <h2 className={styles.reportTitle}>{report.title}</h2>
          {REPORT_INTROS[report.title] && (
            <p className={styles.reportIntro}>{REPORT_INTROS[report.title]}</p>
          )}
          <p className={styles.timestamp}>
            {new Date(report.timestamp).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' })}
          </p>
          {report.sections.map((section, si) => {
            const results = [...section.results].sort((a: Result, b: Result) => b.opsPerSec - a.opsPerSec);
            if (!results.length) return null;

            const fastest = results[0];
            const slowest = results[results.length - 1];
            const speedup = fastest.opsPerSec / slowest.opsPerSec;

            const latencyData = buildLatencyData(results);
            const contenderNames = results.map((r: Result) => r.name);
            const resultByName: Record<string, Result> = {};
            results.forEach((r: Result) => { resultByName[r.name] = r; });
            const colorForName = (n: string) => resultByName[n] ? getColor(resultByName[n]) : '#94a3b8';

            const chartH = Math.max(results.length * 38 + 40, 200);

            return (
              <div key={si} className={styles.section}>
                <h3 className={styles.sectionTitle}>{section.title}</h3>
                <p className={styles.description}>{section.description}</p>
                {section.code && (
                  <details className={styles.codeDetails}>
                    <summary>Show Foundations code</summary>
                    <pre className={styles.codeBlock}><code>{section.code}</code></pre>
                  </details>
                )}

                <div className={styles.chartRow}>
                  {/* Throughput bar chart */}
                  <div className={styles.chartHalf}>
                    <div className={styles.chartLabel}>
                      {section.metricLabel}
                      <span className={styles.chartHint}>&rarr; higher is better</span>
                    </div>
                    <ResponsiveContainer width="100%" height={chartH}>
                      <BarChart data={results} layout="vertical" margin={{ left: 110, right: 60, top: 5, bottom: 5 }}>
                        <XAxis type="number" tickFormatter={formatOps} fontSize={11} />
                        <YAxis type="category" dataKey="name" width={110} tick={{ fontSize: 12 }} />
                        <Tooltip
                          wrapperStyle={{ opacity: 1, zIndex: 1000 }}
                          content={({ active, payload }: any) => {
                            if (!active || !payload?.length) return null;
                            const r: Result = payload[0].payload;
                            return (
                              <div className={styles.tooltip}>
                                <div className={styles.tooltipName}>{r.name}</div>
                                <table className={styles.tooltipTable}>
                                  <tbody>
                                    <tr><td>{section.metricLabel}</td><td>{formatOps(r.opsPerSec)}</td></tr>
                                    <tr><td>wall time</td><td>{r.wallTimeMs.toLocaleString()}ms</td></tr>
                                  </tbody>
                                </table>
                              </div>
                            );
                          }}
                          cursor={{ fill: 'rgba(0,0,0,0.05)' }}
                        />
                        <Bar dataKey="opsPerSec" radius={[0, 4, 4, 0]} barSize={22}>
                          {results.map((r: Result, idx: number) => (
                            <Cell key={idx} fill={getColor(r)} opacity={r.name === fastest.name ? 1 : 0.7} />
                          ))}
                        </Bar>
                      </BarChart>
                    </ResponsiveContainer>
                  </div>

                  {/* Latency line chart */}
                  <div className={styles.chartHalf}>
                    <div className={styles.chartLabel}>
                      Latency
                      <span className={styles.chartHint}>&darr; lower is better</span>
                    </div>
                    <ResponsiveContainer width="100%" height={chartH}>
                      <LineChart data={latencyData} margin={{ left: 10, right: 20, top: 5, bottom: 5 }}>
                        <XAxis dataKey="percentile" tick={{ fontSize: 12 }} />
                        <YAxis tickFormatter={formatDuration} tick={{ fontSize: 11 }} width={55} />
                        <Tooltip
                          wrapperStyle={{ opacity: 1, zIndex: 1000 }}
                          content={({ active, payload, label }: any) => {
                            if (!active || !payload?.length) return null;
                            const sorted = [...payload].sort((a: any, b: any) => a.value - b.value);
                            return (
                              <div className={styles.tooltip}>
                                <div className={styles.tooltipName}>{label}</div>
                                <table className={styles.tooltipTable}>
                                  <tbody>
                                    {sorted.map((p: any) => (
                                      <tr key={p.dataKey}>
                                        <td style={{ color: p.color }}>{p.dataKey}</td>
                                        <td>{formatDuration(p.value)}</td>
                                      </tr>
                                    ))}
                                  </tbody>
                                </table>
                              </div>
                            );
                          }}
                        />
                        <Legend iconType="plainline" wrapperStyle={{ fontSize: 11, paddingTop: 8 }} />
                        {contenderNames.map((name: string) => (
                          <Line
                            key={name}
                            type="monotone"
                            dataKey={name}
                            stroke={colorForName(name)}
                            strokeWidth={name === fastest.name ? 3 : 2}
                            dot={{ r: 3, strokeWidth: 0, fill: colorForName(name) }}
                            activeDot={{ r: 6, strokeWidth: 2, stroke: '#fff' }}
                          />
                        ))}
                      </LineChart>
                    </ResponsiveContainer>
                  </div>
                </div>

                {results.length >= 2 && (() => {
                  const pgpipe = results.find((r: Result) => resolveLibrary(r) === 'Foundations+PgPipe');
                  const hero = pgpipe || fastest;
                  const isWinner = hero.name === fastest.name;
                  const vsFastest = isWinner ? 1 : fastest.opsPerSec / hero.opsPerSec;
                  const vsSlowest = hero.opsPerSec / slowest.opsPerSec;
                  return (
                    <p className={isWinner ? styles.speedupWin : styles.speedupLose}
                       title={isWinner
                         ? `${vsSlowest.toFixed(1)}x faster than ${slowest.name}`
                         : `${vsFastest.toFixed(1)}x behind ${fastest.name}, ${vsSlowest.toFixed(1)}x faster than ${slowest.name}`}>
                      <strong>{hero.name}</strong>{' '}
                      {isWinner
                        ? <span>fastest</span>
                        : <span>{vsFastest.toFixed(1)}x behind {fastest.name}</span>}
                    </p>
                  );
                })()}
              </div>
            );
          })}
        </div>
      ))}
    </>
  );
}
