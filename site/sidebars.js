// @ts-check

/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
  jdbcSidebar: [
    {type: "doc", id: "readme", label: "Getting Started"},
    {type: "doc", id: "row-types", label: "Row Types & Parsers"},
    {type: "doc", id: "result-sets", label: "Result Sets"},
    {type: "doc", id: "fragments", label: "Fragments"},
    {type: "doc", id: "transactors", label: "Transactors"},
    {type: "doc", id: "stored-procedures", label: "Stored Procedures"},
    {type: "doc", id: "query-analysis", label: "Query Analysis"},
    {type: "doc", id: "streaming-inserts", label: "Streaming Inserts"},
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
