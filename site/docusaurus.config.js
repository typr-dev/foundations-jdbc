// @ts-check

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'Foundations JDBC',
  tagline: 'Every type. Every database. Finally.',
  favicon: 'img/favicon.ico',

  url: 'https://foundations.typr.dev',
  baseUrl: '/',

  organizationName: 'typr-dev',
  projectName: 'foundations-jdbc',

  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          sidebarPath: './sidebars.js',
          editUrl: 'https://github.com/typr-dev/foundations-jdbc/tree/main/site/',
        },
        blog: false,
        theme: {
          customCss: './src/css/custom.css',
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      navbar: {
        title: 'Foundations JDBC',
        items: [
          {
            type: 'docSidebar',
            sidebarId: 'jdbcSidebar',
            position: 'left',
            label: 'Docs',
          },
          {
            href: 'https://github.com/typr-dev/foundations-jdbc',
            label: 'GitHub',
            position: 'right',
          },
        ],
      },
      footer: {
        style: 'dark',
        links: [
          {
            title: 'Documentation',
            items: [
              { label: 'Getting Started', to: '/docs/' },
              { label: 'Transactors', to: '/docs/transactors' },
              { label: 'Fragments', to: '/docs/fragments' },
              { label: 'Row Codecs', to: '/docs/row-codecs' },
              { label: 'Templates', to: '/docs/templates' },
            ],
          },
          {
            title: 'Database Types',
            items: [
              { label: 'PostgreSQL', to: '/docs/postgresql' },
              { label: 'MariaDB/MySQL', to: '/docs/mariadb' },
              { label: 'DuckDB', to: '/docs/duckdb' },
              { label: 'Oracle', to: '/docs/oracle' },
              { label: 'SQL Server', to: '/docs/sqlserver' },
              { label: 'DB2', to: '/docs/db2' },
            ],
          },
          {
            title: 'More',
            items: [
              {
                label: 'GitHub',
                href: 'https://github.com/typr-dev/foundations-jdbc',
              },
              {
                label: 'Typr',
                href: 'https://typr.dev',
              },
            ],
          },
        ],
        copyright: `Copyright © ${new Date().getFullYear()} Øyvind Raddum Berg. MIT License.`,
      },
      prism: {
        theme: require('prism-react-renderer').themes.github,
        darkTheme: require('prism-react-renderer').themes.dracula,
        additionalLanguages: ['java', 'scala', 'kotlin', 'yaml', 'sql'],
      },
    }),
};

module.exports = config;
