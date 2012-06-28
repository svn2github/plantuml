/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2012, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 7715 $
 *
 */
package net.sourceforge.plantuml.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

import net.sourceforge.plantuml.version.Version;

public class PDatabaseMetaData extends TraceObject implements DatabaseMetaData {

	public static final String TABLE_PLANTUML_VERSION = "PLANTUML_VERSION";
	public static final String TABLE_DIAGRAM = "DIAGRAM";

	public static final String CATALOG2 = "Catalog2";
	public static final String SCHEMA1 = "Schema1";
	private String url = "http://plantuml.sourceforge.net/";
	final private String username;

	public PDatabaseMetaData(String username) {
		this.username = username;
	}

	public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern)
			throws SQLException {
		trace(1, catalog, schemaPattern, tableNamePattern);
		return null;
	}

	public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
		trace(1, catalog, schema, table);
		return null;
	}

	public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
			throws SQLException {
		trace(1, catalog, schema, table, columnNamePattern);
		return null;
	}

	public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
		trace(1, catalog, schema, table);
		final PResultSetMetaData meta = new PResultSetMetaData(new PName("TABLE_CAT", PType.VARCHAR), new PName(
				"TABLE_SCHEM", PType.VARCHAR), new PName("TABLE_NAME", PType.VARCHAR), new PName("COLUMN_NAME",
				PType.VARCHAR), new PName("KEY_SEQ", PType.INTEGER), new PName("PK_NAME", PType.VARCHAR));
		PTable ptable = new PTable(meta);
		if (table.contains(TABLE_PLANTUML_VERSION)) {
			ptable = PlantumlDriver.PlantumlVersionTable.getPrimaryKeys(meta);
		} else if (table.contains(TABLE_DIAGRAM)) {
			ptable = PlantumlDriver.DiagramTable.getPrimaryKeys(meta);
		} else {
			log(1, "ERROR " + table);
		}
		return new PResultSet(ptable);
	}

	public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
			throws SQLException {
		trace(1, catalog, schemaPattern, tableNamePattern, columnNamePattern);
		final PResultSetMetaData meta = new PResultSetMetaData(new PName("TABLE_CAT", PType.VARCHAR), new PName(
				"TABLE_SCHEM", PType.VARCHAR), new PName("TABLE_NAME", PType.VARCHAR), new PName("COLUMN_NAME",
				PType.VARCHAR), new PName("DATA_TYPE", PType.INTEGER), new PName("TYPE_NAME", PType.VARCHAR),
				new PName("COLUMN_SIZE", PType.INTEGER), new PName("BUFFER_LENGTH", PType.INTEGER), new PName(
						"DECIMAL_DIGITS", PType.INTEGER), new PName("NUM_PREC_RADIX", PType.INTEGER), new PName(
						"NULLABLE", PType.INTEGER), new PName("REMARKS", PType.VARCHAR), new PName("COLUMN_DEF",
						PType.VARCHAR), new PName("SQL_DATA_TYPE", PType.INTEGER), new PName("SQL_DATETIME_SUB",
						PType.INTEGER), new PName("CHAR_OCTET_LENGTH", PType.INTEGER), new PName("ORDINAL_POSITION",
						PType.INTEGER), new PName("IS_NULLABLE", PType.VARCHAR), new PName("SCOPE_CATLOG",
						PType.VARCHAR), new PName("SCOPE_SCHEMA", PType.VARCHAR), new PName("SCOPE_TABLE",
						PType.VARCHAR), new PName("SOURCE_DATA_TYPE", PType.INTEGER), new PName("IS_AUTOINCREMENT",
						PType.VARCHAR));
		PTable ptable = new PTable(meta);
		if (tableNamePattern.contains(TABLE_PLANTUML_VERSION)) {
			ptable = PlantumlDriver.PlantumlVersionTable.getColumns(meta);
		} else if (tableNamePattern.contains(TABLE_DIAGRAM)) {
			ptable = PlantumlDriver.DiagramTable.getColumns(meta);
		} else {
			log(1, "ERROR " + tableNamePattern);
		}
		return new PResultSet(ptable);
	}

	public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable)
			throws SQLException {
		trace(0, catalog, schema, table, "scope=" + scope, "nullable=" + nullable);
		final PResultSetMetaData meta = new PResultSetMetaData(new PName("SCOPE", PType.INTEGER), new PName(
				"COLUMN_NAME", PType.VARCHAR), new PName("DATA_TYPE", PType.INTEGER), new PName("TYPE_NAME",
				PType.VARCHAR), new PName("COLUMN_SIZE", PType.INTEGER), new PName("BUFFER_LENGTH", PType.INTEGER),
				new PName("DECIMAL_DIGITS", PType.INTEGER), new PName("PSEUDO_COLUMN", PType.VARCHAR));
		PTable ptable = new PTable(meta);
		if (TABLE_PLANTUML_VERSION.equals(table)) {
			ptable = PlantumlDriver.PlantumlVersionTable.asBestRowIdentifier(meta);
		} else if (TABLE_DIAGRAM.equals(table)) {
			ptable = PlantumlDriver.DiagramTable.asBestRowIdentifier(meta);
		} else {
			log(1, "ERROR getBestRowIdentifier " + table);
			return null;
		}
		return new PResultSet(ptable);
	}

	public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
			throws SQLException {
		trace(0, catalog, schemaPattern, tableNamePattern);
		final PResultSetMetaData meta = new PResultSetMetaData(new PName("TABLE_CAT", PType.VARCHAR), new PName(
				"TABLE_SCHEM", PType.VARCHAR), new PName("TABLE_NAME", PType.VARCHAR), new PName("TABLE_TYPE",
				PType.VARCHAR), new PName("REMARKS", PType.VARCHAR), new PName("TYPE_CAT", PType.VARCHAR), new PName(
				"TYPE_SCHEM", PType.VARCHAR), new PName("TYPE_NAME", PType.VARCHAR), new PName(
				"SELF_REFERENCING_COL_NAME", PType.VARCHAR), new PName("REF_GENERATION", PType.BOOLEAN));
		final PTable table = new PTable(meta);
		table.addLine(new PLine(new PString(CATALOG2), new PString(SCHEMA1), new PString(TABLE_PLANTUML_VERSION),
				new PString("TABLE"), new PString("Information about PlantUML"), null, null, null, null, null));
		table.addLine(new PLine(new PString(CATALOG2), new PString(SCHEMA1), new PString(TABLE_DIAGRAM), new PString(
				"TABLE"), new PString("UML diagrams"), null, null, null, null, null));
		return new PResultSet(table);
	}

	public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
			throws SQLException {
		trace(0);
		final PResultSetMetaData meta = new PResultSetMetaData(new PName("PROCEDURE_CAT", PType.VARCHAR), new PName(
				"PROCEDURE_SCHEM", PType.VARCHAR), new PName("PROCEDURE_NAME", PType.VARCHAR), new PName("reserved1",
				PType.VARCHAR), new PName("reserved2", PType.VARCHAR), new PName("reserved3", PType.VARCHAR),
				new PName("REMARKS", PType.VARCHAR), new PName("PROCEDURE_TYPE", PType.INTEGER), new PName(
						"SPECIFIC_NAME", PType.VARCHAR));
		final PTable table = new PTable(meta);
		return new PResultSet(table);
	}

	public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		trace(0, catalog, schemaPattern, tableNamePattern);
		final PResultSetMetaData meta = new PResultSetMetaData(new PName("TABLE_CAT", PType.VARCHAR), new PName(
				"TABLE_SCHEM", PType.VARCHAR), new PName("TABLE_NAME", PType.VARCHAR), new PName("SUPERTABLE_NAME",
				PType.VARCHAR));
		final PTable table = new PTable(meta);
		return new PResultSet(table);
	}

	public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)
			throws SQLException {
		trace(1);
		final PResultSetMetaData meta = new PResultSetMetaData(new PName("TYPE_CAT", PType.VARCHAR), new PName(
				"TYPE_SCHEM", PType.VARCHAR), new PName("TYPE_NAME", PType.VARCHAR), new PName("CLASS_NAME",
				PType.VARCHAR), new PName("DATA_TYPE", PType.INTEGER), new PName("REMARKS", PType.VARCHAR), new PName(
				"BASE_TYPE", PType.INTEGER));
		final PTable table = new PTable(meta);
		return new PResultSet(table);
	}

	public ResultSet getTypeInfo() throws SQLException {
		trace(0);
		final PResultSetMetaData meta = new PResultSetMetaData(new PName("TYPE_NAME", PType.VARCHAR), new PName(
				"DATA_TYPE", PType.INTEGER), new PName("PRECISION", PType.INTEGER), new PName("LITERAL_PREFIX",
				PType.VARCHAR), new PName("LITERAL_SUFFIX", PType.VARCHAR), new PName("CREATE_PARAMS", PType.VARCHAR),
				new PName("NULLABLE", PType.INTEGER), new PName("CASE_SENSITIVE", PType.BOOLEAN), new PName(
						"SEARCHABLE", PType.BOOLEAN), new PName("UNSIGNED_ATTRIBUTE", PType.BOOLEAN), new PName(
						"FIXED_PREC_SCALE", PType.BOOLEAN), new PName("AUTO_INCREMENT", PType.BOOLEAN), new PName(
						"LOCAL_TYPE_NAME", PType.VARCHAR), new PName("MINIMUM_SCALE", PType.INTEGER), new PName(
						"MAXIMUM_SCALE", PType.INTEGER), new PName("SQL_DATA_TYPE", PType.INTEGER), new PName(
						"SQL_DATETIME_SUB", PType.INTEGER), new PName("NUM_PREC_RADIX", PType.INTEGER));
		final PTable table = new PTable(meta);
		// table.addLine(new PLine(new PString("VARCHAR"), new PInteger(Types.VARCHAR), new PInteger(65000), null, null,
		// null, new PInteger(typeNullableUnknown), new PBoolean(true), new PInteger(typePredNone), new PBoolean(
		// false), new PBoolean(false), new PBoolean(false), null, new PInteger(0), new PInteger(65000),
		// new PInteger(0), new PInteger(0), new PInteger(10)));
		return new PResultSet(table);
	}

	public ResultSet getTableTypes() throws SQLException {
		trace(0);
		final PResultSetMetaData meta = new PResultSetMetaData(new PName("TABLE_TYPE", PType.VARCHAR));
		final PTable table = new PTable(meta);
		table.addLine(new PLine(new PString("TABLE")));
		return new PResultSet(table);
	}

	public ResultSet getSchemas() throws SQLException {
		trace(0);
		final PResultSetMetaData meta = new PResultSetMetaData(new PName("TABLE_SCHEM", PType.VARCHAR), new PName(
				"TABLE_CATALOG", PType.VARCHAR));
		final PTable data = new PTable(meta);
		data.addLine(new PLine(new PString(SCHEMA1), new PString(CATALOG2)));
		return new PResultSet(data);
	}

	public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
		trace(1, catalog, schemaPattern);
		return null;
	}

	public ResultSet getCatalogs() throws SQLException {
		trace(0);
		final PResultSetMetaData meta = new PResultSetMetaData(new PName("TABLE_CAT", PType.VARCHAR));
		final PTable data = new PTable(meta);
		data.addLine(new PLine(new PString(CATALOG2)));
		return new PResultSet(data);
	}

	public ResultSet getClientInfoProperties() throws SQLException {
		trace(0);
		final PResultSetMetaData meta = new PResultSetMetaData(new PName("NAME", PType.VARCHAR), new PName("MAX_LEN",
				PType.INTEGER), new PName("DEFAULT_VALUE", PType.VARCHAR), new PName("DESCRIPTION", PType.VARCHAR));
		final PTable data = new PTable(meta);
		data.addLine(new PLine(new PString("Trace"), new PInteger(81), new PString("FALSE"), new PString(
				"Set to TRUE to enable debug")));
		return new PResultSet(data);
	}

	public boolean allProceduresAreCallable() throws SQLException {
		trace(0);
		return false;
	}

	public boolean allTablesAreSelectable() throws SQLException {
		trace(0);
		return false;
	}

	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
		trace(0);
		return false;
	}

	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		trace(0);
		return false;
	}

	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		trace(0);
		return false;
	}

	public boolean deletesAreDetected(int type) throws SQLException {
		trace(1);
		return false;
	}

	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		trace(0);
		return false;
	}

	public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern,
			String attributeNamePattern) throws SQLException {
		trace(1);
		return null;
	}

	public String getCatalogSeparator() throws SQLException {
		trace(0);
		return "-";
	}

	public String getCatalogTerm() throws SQLException {
		trace(0);
		return "catalog";
	}

	public Connection getConnection() throws SQLException {
		trace(1);
		return null;
	}

	public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable,
			String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
		trace(1);
		return null;
	}

	public int getDatabaseMajorVersion() throws SQLException {
		trace(0);
		return 1;
	}

	public int getDatabaseMinorVersion() throws SQLException {
		trace(0);
		return 0;
	}

	public String getDatabaseProductName() throws SQLException {
		trace(0);
		return "PlantUML";
	}

	public String getDatabaseProductVersion() throws SQLException {
		trace(0);
		return "Version " + Version.version();
	}

	public int getDefaultTransactionIsolation() throws SQLException {
		trace(0);
		return Connection.TRANSACTION_NONE;
	}

	public int getDriverMajorVersion() {
		trace(0);
		return 1;
	}

	public int getDriverMinorVersion() {
		trace(0);
		return Version.version();
	}

	public String getDriverName() throws SQLException {
		trace(0);
		return "PlantUML driver";
	}

	public String getDriverVersion() throws SQLException {
		trace(0);
		return "" + Version.version();
	}

	public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
		trace(1);
		return null;
	}

	public String getExtraNameCharacters() throws SQLException {
		trace(0);
		return "";
	}

	public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern,
			String columnNamePattern) throws SQLException {
		trace(1);
		return null;
	}

	public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
		trace(1);
		return null;
	}

	public String getIdentifierQuoteString() throws SQLException {
		trace(0);
		return " ";
	}

	public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
		trace(1);
		return null;
	}

	public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate)
			throws SQLException {
		trace(1);
		return null;
	}

	public int getJDBCMajorVersion() throws SQLException {
		trace(0);
		return 1;
	}

	public int getJDBCMinorVersion() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxBinaryLiteralLength() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxCatalogNameLength() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxCharLiteralLength() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxColumnNameLength() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxColumnsInGroupBy() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxColumnsInIndex() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxColumnsInOrderBy() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxColumnsInSelect() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxColumnsInTable() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxConnections() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxCursorNameLength() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxIndexLength() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxProcedureNameLength() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxRowSize() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxSchemaNameLength() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxStatementLength() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxStatements() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxTableNameLength() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxTablesInSelect() throws SQLException {
		trace(0);
		return 0;
	}

	public int getMaxUserNameLength() throws SQLException {
		trace(0);
		return 0;
	}

	public String getNumericFunctions() throws SQLException {
		trace(0);
		return "";
	}

	public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern,
			String columnNamePattern) throws SQLException {
		trace(1);
		return null;
	}

	public String getProcedureTerm() throws SQLException {
		trace(0);
		return "procedure";
	}

	public int getResultSetHoldability() throws SQLException {
		trace(0);
		return ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	public RowIdLifetime getRowIdLifetime() throws SQLException {
		trace(0);
		return RowIdLifetime.ROWID_UNSUPPORTED;
	}

	public String getSQLKeywords() throws SQLException {
		trace(0);
		return "";
	}

	public int getSQLStateType() throws SQLException {
		trace(0);
		return sqlStateSQL;
	}

	public String getSchemaTerm() throws SQLException {
		trace(0);
		return "schema";
	}

	public String getSearchStringEscape() throws SQLException {
		trace(0);
		return "/";
	}

	public String getStringFunctions() throws SQLException {
		trace(0);
		return "";
	}

	public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
		trace(1);
		return null;
	}

	public String getSystemFunctions() throws SQLException {
		trace(1);
		return null;
	}

	public String getTimeDateFunctions() throws SQLException {
		trace(0);
		return "";
	}

	public String getURL() throws SQLException {
		trace(0);
		log(1, "url=" + url);
		return url;
	}

	public String getUserName() throws SQLException {
		trace(0);
		return username;
	}

	public boolean insertsAreDetected(int type) throws SQLException {
		trace(1);
		return false;
	}

	public boolean isCatalogAtStart() throws SQLException {
		trace(0);
		return false;
	}

	public boolean isReadOnly() throws SQLException {
		trace(0);
		return false;
	}

	public boolean locatorsUpdateCopy() throws SQLException {
		trace(0);
		return false;
	}

	public boolean nullPlusNonNullIsNull() throws SQLException {
		trace(0);
		return false;
	}

	public boolean nullsAreSortedAtEnd() throws SQLException {
		trace(0);
		return false;
	}

	public boolean nullsAreSortedAtStart() throws SQLException {
		trace(0);
		return false;
	}

	public boolean nullsAreSortedHigh() throws SQLException {
		trace(0);
		return false;
	}

	public boolean nullsAreSortedLow() throws SQLException {
		trace(0);
		return false;
	}

	public boolean othersDeletesAreVisible(int type) throws SQLException {
		trace(1);
		return false;
	}

	public boolean othersInsertsAreVisible(int type) throws SQLException {
		trace(1);
		return false;
	}

	public boolean othersUpdatesAreVisible(int type) throws SQLException {
		trace(1);
		return false;
	}

	public boolean ownDeletesAreVisible(int type) throws SQLException {
		trace(1);
		return false;
	}

	public boolean ownInsertsAreVisible(int type) throws SQLException {
		trace(1);
		return false;
	}

	public boolean ownUpdatesAreVisible(int type) throws SQLException {
		trace(1);
		return false;
	}

	public boolean storesLowerCaseIdentifiers() throws SQLException {
		trace(0);
		return false;
	}

	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		trace(0);
		return false;
	}

	public boolean storesMixedCaseIdentifiers() throws SQLException {
		trace(0);
		return false;
	}

	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		trace(0);
		return false;
	}

	public boolean storesUpperCaseIdentifiers() throws SQLException {
		trace(0);
		return false;
	}

	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsANSI92FullSQL() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsBatchUpdates() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsColumnAliasing() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsConvert() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsConvert(int fromType, int toType) throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsCoreSQLGrammar() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsCorrelatedSubqueries() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsExpressionsInOrderBy() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsExtendedSQLGrammar() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsFullOuterJoins() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsGetGeneratedKeys() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsGroupBy() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsGroupByBeyondSelect() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsGroupByUnrelated() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsLikeEscapeClause() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsLimitedOuterJoins() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsMinimumSQLGrammar() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsMultipleOpenResults() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsMultipleResultSets() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsMultipleTransactions() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsNamedParameters() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsNonNullableColumns() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsOrderByUnrelated() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsOuterJoins() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsPositionedDelete() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsPositionedUpdate() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsResultSetHoldability(int holdability) throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsResultSetType(int type) throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsSavepoints() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsSchemasInDataManipulation() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsSelectForUpdate() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsStatementPooling() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsStoredProcedures() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsSubqueriesInComparisons() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsSubqueriesInExists() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsSubqueriesInIns() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsTableCorrelationNames() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
		trace(1);
		return false;
	}

	public boolean supportsTransactions() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsUnion() throws SQLException {
		trace(0);
		return false;
	}

	public boolean supportsUnionAll() throws SQLException {
		trace(0);
		return false;
	}

	public boolean updatesAreDetected(int type) throws SQLException {
		trace(1);
		return false;
	}

	public boolean usesLocalFilePerTable() throws SQLException {
		trace(0);
		return false;
	}

	public boolean usesLocalFiles() throws SQLException {
		trace(0);
		return false;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		trace(1);
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		trace(1);
		return null;
	}

}
