// @ts-check

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'Foundations JDBC',
  tagline: 'A JDBC wrapper library with perfect type modeling for all databases',
  favicon: 'img/favicon.ico',

  url: 'https://typr-dev.github.io',
  baseUrl: '/foundations-jdbc/',

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
          routeBasePath: '/',
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
            title: 'Docs',
            items: [
              {
                label: 'Introduction',
                to: '/',
              },
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
        copyright: `Copyright © ${new Date().getFullYear()} Øyvind Raddum Berg. Built with Docusaurus.`,
      },
      prism: {
        theme: require('prism-react-renderer').themes.github,
        darkTheme: require('prism-react-renderer').themes.dracula,
        additionalLanguages: ['java', 'scala', 'kotlin', 'yaml', 'sql'],
      },
    }),
};

module.exports = config;
