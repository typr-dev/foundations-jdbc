// @ts-check

/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
  jdbcSidebar: [
    {
      type: "category",
      label: "Guide",
      collapsed: false,
      items: [
        {type: "doc", id: "readme", label: "Getting Started"},
        {type: "doc", id: "fragments", label: "Fragments"},
        {type: "doc", id: "row-codecs", label: "Row Codecs"},
        {type: "doc", id: "operations", label: "Operations"},
        {type: "doc", id: "transactors", label: "Transactors"},
        {type: "doc", id: "query-analysis", label: "Query Analysis"},
        {type: "doc", id: "json", label: "JSON"},
        {type: "doc", id: "stored-procedures", label: "Stored Procedures"},
      ],
    },
    {
      type: "category",
      label: "Going to Production",
      collapsed: false,
      items: [
        {type: "doc", id: "error-handling", label: "Error Handling"},
        {type: "doc", id: "connection-pooling", label: "Connection Pooling"},
        {type: "doc", id: "spring-boot", label: "Spring Boot"},
        {type: "doc", id: "thread-safety", label: "Thread Safety"},
        {type: "doc", id: "structuring-repositories", label: "Structuring Repositories"},
        {type: "doc", id: "observability", label: "Observability"},
        {type: "doc", id: "virtual-threads", label: "Virtual Threads"},
      ],
    },
    {
      type: "category",
      label: "Advanced",
      collapsed: false,
      items: [
        {type: "doc", id: "templates", label: "Templates"},
        {type: "doc", id: "composing-operations", label: "Composing Operations"},
        {type: "doc", id: "strategies", label: "Strategies"},
        {type: "doc", id: "query-analysis-reference", label: "Query Analysis Reference"},
        {type: "doc", id: "streaming-inserts", label: "Streaming Inserts"},
        {type: "doc", id: "kotlin-interpolation", label: "Kotlin String Interpolation"},
      ],
    },
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
