package com.tightdb.lib;

import com.tightdb.TableBase;
import com.tightdb.TableQuery;
import com.tightdb.TableViewBase;

public abstract class AbstractColumn<Type, Cursor, Query> {

	protected final EntityTypes<?, ?, Cursor, Query> types;
	protected final AbstractCursor<Cursor> cursor;
	protected final String name;
	protected final int columnIndex;
	protected final TableQuery query;
	protected final IRowsetBase rowset;

	public AbstractColumn(EntityTypes<?, ?, Cursor, Query> types, AbstractCursor<Cursor> cursor, int index, String name) {
		this(types, cursor.rowset, cursor, index, name);
	}

	public AbstractColumn(EntityTypes<?, ?, Cursor, Query> types, TableQuery query, int index, String name) {
		this(types, query.table, query, index, name);
	}

	public AbstractColumn(EntityTypes<?, ?, Cursor, Query> types, IRowsetBase rowset, AbstractCursor<Cursor> cursor, int index, String name) {
		this.types = types;
		this.rowset = rowset;
		this.query = null;
		this.cursor = cursor;
		this.columnIndex = index;
		this.name = name;
	}

	public AbstractColumn(EntityTypes<?, ?, Cursor, Query> types, IRowsetBase rowset, TableQuery query, int index, String name) {
		this.types = types;
		this.rowset = rowset;
		this.query = query;
		this.cursor = null;
		this.columnIndex = index;
		this.name = name;
	}

	protected Type get() {
		throw new UnsupportedOperationException("Cannot get the column's value!");
	}

	protected void set(Type value) {
		throw new UnsupportedOperationException("Cannot set the column's value!");
	}

	@Override
	public String toString() {
		return cursor + "." + name;
	}

	public String getName() {
		return name;
	}

	public String getReadable() {
		try {
			return String.valueOf(get());
		} catch (Exception e) {
			return "ERROR!";
		}
	}

	private TableBase table() {
		return (TableBase) rowset;
	}

	protected TableQuery getQuery() {
		return query != null ? query : new TableQuery(table());
	}

	protected Query query(TableQuery tableQuery) {
		try {
			return types.getQueryClass().getConstructor(TableBase.class, TableQuery.class).newInstance(table(), tableQuery);
		} catch (Exception e) {
			throw new RuntimeException("Cannot create a query!", e);
		}
	}

	protected TableViewBase getView() {
		return new TableViewBase(table()); // FIXME: finish this for specified
											// queries
	}

}
