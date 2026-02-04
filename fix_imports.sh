#!/bin/bash

for f in $(find documentation-examples-scala -name "*.scala"); do
  # Check if file needs the import (uses any foundations types)
  if grep -qE "PgType|PgTypes|MariaType|MariaTypes|DuckDbType|DuckDbTypes|OracleType|OracleTypes|SqlServerType|SqlServerTypes|Db2Type|Db2Types|Fragment|RowParser|And|Transactor|QueryAnalysis|QueryAnalyzer|Operation|DbType|Jsonb|Json|Range|Money|Vector|Xml" "$f"; then
    # Check if import already exists
    if grep -q "import dev.typr.scalafoundations" "$f"; then
      echo "Already has import: $f"
    else
      # Add import after package line
      sed -i '' '/^package /a\
import dev.typr.scalafoundations.*
' "$f"
      echo "Added import to: $f"
    fi
  fi
done
