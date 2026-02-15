// @ts-check

/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
  jdbcSidebar: [
    {type: "doc", id: "readme", label: "Getting Started"},
    {type: "doc", id: "fragments", label: "Fragments"},
    {type: "doc", id: "transactors", label: "Transactors"},
    {type: "doc", id: "named-row-parsers", label: "Named Row Parsers"},
    {type: "doc", id: "query-analysis", label: "Query Analysis"},
    {type: "doc", id: "stored-procedures", label: "Stored Procedures"},
    {type: "doc", id: "streaming-inserts", label: "Streaming Inserts"},
    {type: "doc", id: "sql-templates", label: "SQL Templates"},
    {type: "doc", id: "composing-operations", label: "Composing Operations"},
    {type: "doc", id: "production-patterns", label: "Production Patterns"},
    {type: "doc", id: "kotlin-interpolation", label: "Kotlin String Interpolation"},
    {
      type: "category",
      label: "Database Types",
      collapsed: false,
      link: {type: "doc", id: "database-types"},
      items: [
        {type: "doc", id: "postgresql", label: "PostgreSQL"},
        {type: "doc", id: "mariadb", label: "MariaDB/MySQL"},
        {type: "doc", id: "duckdb", label: "DuckDB"},
        {type: "doc", id: "oracle", label: "Oracle"},
        {type: "doc", id: "sqlserver", label: "SQL Server"},
        {type: "doc", id: "db2", label: "DB2"},
      ],
    },
  ]
};

module.exports = sidebars;
