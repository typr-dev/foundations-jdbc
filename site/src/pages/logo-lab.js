import React from 'react';
import Layout from '@theme/Layout';

const AMBER = '#c2410c';

/* ------------------------------------------------------------------
   Ten variations on `::`
   ------------------------------------------------------------------ */

/* V1 — current: squoval dots, tight pairs */
const V1 = () => (
  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill={AMBER}>
    <rect x="7.3"  y="8.9"  width="2.4" height="2.4" rx="0.4" />
    <rect x="7.3"  y="12.7" width="2.4" height="2.4" rx="0.4" />
    <rect x="14.3" y="8.9"  width="2.4" height="2.4" rx="0.4" />
    <rect x="14.3" y="12.7" width="2.4" height="2.4" rx="0.4" />
  </svg>
);

/* V2 — round dots, tight pairs */
const V2 = () => (
  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill={AMBER}>
    <circle cx="8.5"  cy="10" r="1.3" />
    <circle cx="8.5"  cy="14" r="1.3" />
    <circle cx="15.5" cy="10" r="1.3" />
    <circle cx="15.5" cy="14" r="1.3" />
  </svg>
);

/* V3 — hard squares, no rounding */
const V3 = () => (
  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill={AMBER}>
    <rect x="7.3"  y="8.9"  width="2.4" height="2.4" />
    <rect x="7.3"  y="12.7" width="2.4" height="2.4" />
    <rect x="14.3" y="8.9"  width="2.4" height="2.4" />
    <rect x="14.3" y="12.7" width="2.4" height="2.4" />
  </svg>
);

/* V4 — large bold squovals */
const V4 = () => (
  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill={AMBER}>
    <rect x="6.9"  y="8"    width="3.2" height="3.2" rx="0.5" />
    <rect x="6.9"  y="12.8" width="3.2" height="3.2" rx="0.5" />
    <rect x="13.9" y="8"    width="3.2" height="3.2" rx="0.5" />
    <rect x="13.9" y="12.8" width="3.2" height="3.2" rx="0.5" />
  </svg>
);

/* V5 — small delicate circles */
const V5 = () => (
  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill={AMBER}>
    <circle cx="9.5"  cy="10.5" r="1.0" />
    <circle cx="9.5"  cy="13.5" r="1.0" />
    <circle cx="14.5" cy="10.5" r="1.0" />
    <circle cx="14.5" cy="13.5" r="1.0" />
  </svg>
);

/* V6 — tall pills (vertical emphasis) */
const V6 = () => (
  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill={AMBER}>
    <rect x="7.5"  y="8.4"  width="2" height="3" rx="1" />
    <rect x="7.5"  y="12.6" width="2" height="3" rx="1" />
    <rect x="14.5" y="8.4"  width="2" height="3" rx="1" />
    <rect x="14.5" y="12.6" width="2" height="3" rx="1" />
  </svg>
);

/* V7 — ligature: each colon's dots connected by thin vertical bar */
const V7 = () => (
  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill={AMBER}>
    {/* left colon */}
    <rect x="7.8"  y="8.5" width="1.4" height="7" rx="0.7" />
    <circle cx="8.5"  cy="8.5"  r="1.35" />
    <circle cx="8.5"  cy="15.5" r="1.35" />
    {/* right colon */}
    <rect x="14.8" y="8.5" width="1.4" height="7" rx="0.7" />
    <circle cx="15.5" cy="8.5"  r="1.35" />
    <circle cx="15.5" cy="15.5" r="1.35" />
  </svg>
);

/* V8 — real programming-mono text `::` */
const V8 = () => (
  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <text x="12" y="18"
          fontFamily="'Geist Mono', 'JetBrains Mono', Menlo, Consolas, 'Courier New', monospace"
          fontSize="22"
          fontWeight="600"
          textAnchor="middle"
          letterSpacing="-0.18em"
          fill={AMBER}>::</text>
  </svg>
);

/* V9 — real serif text `::` */
const V9 = () => (
  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <text x="12" y="19"
          fontFamily="'Iowan Old Style', Charter, Georgia, 'Times New Roman', serif"
          fontSize="24"
          fontWeight="500"
          textAnchor="middle"
          letterSpacing="-0.2em"
          fill={AMBER}>::</text>
  </svg>
);

/* V10 — diamonds (rotated squares) */
const V10 = () => (
  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill={AMBER}>
    <g transform="translate(8.5,10) rotate(45)">
      <rect x="-1.3" y="-1.3" width="2.6" height="2.6" />
    </g>
    <g transform="translate(8.5,14) rotate(45)">
      <rect x="-1.3" y="-1.3" width="2.6" height="2.6" />
    </g>
    <g transform="translate(15.5,10) rotate(45)">
      <rect x="-1.3" y="-1.3" width="2.6" height="2.6" />
    </g>
    <g transform="translate(15.5,14) rotate(45)">
      <rect x="-1.3" y="-1.3" width="2.6" height="2.6" />
    </g>
  </svg>
);

const VARIATIONS = [
  { id: 'V1',  label: 'Squoval · tight (current)',       body: 'Baseline. Rounded squares, clear code-font feel.',                                                 Component: V1 },
  { id: 'V2',  label: 'Round · tight',                   body: 'Same spacing, circular dots. More organic, less code-ish.',                                        Component: V2 },
  { id: 'V3',  label: 'Hard square',                     body: 'No corner rounding. Pixel-y, very digital.',                                                       Component: V3 },
  { id: 'V4',  label: 'Bold large squoval',              body: 'Thicker presence at small sizes, heavier overall.',                                                Component: V4 },
  { id: 'V5',  label: 'Small delicate circles',          body: 'Refined, recedes. May look weak at favicon size.',                                                 Component: V5 },
  { id: 'V6',  label: 'Tall pills',                      body: 'Vertical pill shapes emphasize each colon as a column.',                                           Component: V6 },
  { id: 'V7',  label: 'Ligature (connected dots)',       body: 'Each colon becomes one barbell. Resolves the 4-dot ambiguity.',                                    Component: V7 },
  { id: 'V8',  label: 'Programming-mono text',           body: 'Real typographic rendering. Uses Geist Mono / Menlo / Consolas via system fallback.',              Component: V8 },
  { id: 'V9',  label: 'Serif text',                      body: 'Real serif rendering (Iowan / Georgia). Matches the Lora wordmark.',                               Component: V9 },
  { id: 'V10', label: 'Diamonds',                        body: 'Rotated squares. Distinct geometric character.',                                                   Component: V10 },
];

/* ------------------------------------------------------------------
   Card displaying a variation at three sizes
   ------------------------------------------------------------------ */
const Card = ({ id, label, body, Component }) => (
  <div style={{
    padding: '1.75rem 1.5rem 1.5rem',
    border: '1px solid rgba(10, 9, 8, 0.1)',
    borderRadius: 14,
    background: '#faf7ee',
    display: 'flex',
    flexDirection: 'column',
    gap: '1.1rem',
  }}>
    <div style={{
      fontFamily: "'Geist Mono', ui-monospace, monospace",
      fontSize: 11,
      letterSpacing: '0.24em',
      textTransform: 'uppercase',
      color: '#7a6f63',
    }}>
      {id} · {label}
    </div>
    <div style={{ display: 'flex', alignItems: 'flex-end', gap: '1.75rem', padding: '0.5rem 0' }}>
      <div style={{ width: 96, height: 96 }}><Component /></div>
      <div style={{ width: 32, height: 32 }}><Component /></div>
      <div style={{ width: 16, height: 16 }}><Component /></div>
    </div>
    <div style={{
      display: 'flex',
      alignItems: 'center',
      gap: '0.6rem',
      padding: '0.5rem 0.75rem',
      border: '1px solid rgba(10, 9, 8, 0.08)',
      borderRadius: 8,
      background: '#f2ecde',
    }}>
      <div style={{ width: 22, height: 22, flexShrink: 0 }}><Component /></div>
      <div style={{
        fontFamily: "'Lora', Georgia, serif",
        fontSize: 15,
        fontWeight: 500,
        color: '#0a0908',
        letterSpacing: '-0.015em',
      }}>
        Foundations JDBC
      </div>
    </div>
    <p style={{
      margin: 0,
      fontSize: 13,
      lineHeight: 1.55,
      color: '#48413a',
    }}>
      {body}
    </p>
  </div>
);

/* ------------------------------------------------------------------
   Page
   ------------------------------------------------------------------ */
export default function LogoLab() {
  return (
    <Layout title="Logo Lab" description="Comparing logo variations">
      <main style={{
        padding: '3rem 1.5rem 5rem',
        maxWidth: 1200,
        margin: '0 auto',
        color: '#1c1815',
      }}>
        <header style={{ marginBottom: '2.5rem', maxWidth: 720 }}>
          <div style={{
            fontFamily: "'Geist Mono', monospace",
            fontSize: 12,
            letterSpacing: '0.24em',
            textTransform: 'uppercase',
            color: '#7a6f63',
            marginBottom: '0.6rem',
          }}>
            § Lab · Ten variations on <code>::</code>
          </div>
          <h1 style={{
            fontFamily: "'Lora', Georgia, serif",
            fontSize: '2.4rem',
            fontWeight: 500,
            letterSpacing: '-0.02em',
            margin: '0 0 0.8rem',
            color: '#0a0908',
          }}>
            Pick the <em style={{ color: AMBER, fontStyle: 'italic' }}>closest</em>.
          </h1>
          <p style={{ fontSize: 16, lineHeight: 1.6, color: '#3a3632', margin: 0 }}>
            Each card shows the mark at hero size (96px), navbar size (32px), and favicon size (16px),
            plus paired with the wordmark. Tell me which ID reads best as a logo — or call out what to
            blend from one into another.
          </p>
        </header>
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))',
          gap: '1.25rem',
        }}>
          {VARIATIONS.map((v) => <Card key={v.id} {...v} />)}
        </div>
      </main>
    </Layout>
  );
}
