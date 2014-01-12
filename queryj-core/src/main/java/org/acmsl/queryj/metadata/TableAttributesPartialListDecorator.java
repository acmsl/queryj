/*
                        QueryJ Core

    Copyright (C) 2002-today  Jose San Leandro Armendariz
                              chous@acm-sl.org

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    Thanks to ACM S.L. for distributing this library under the GPL license.
    Contact info: jose.sanleandro@acm-sl.com

 ******************************************************************************
 *
 * Filename: TableAttributesPartialListDecorator.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Partial list decorator for table attributes.
 *
 * Date: 2013/12/30
 * Time: 10:25
 *
 */
package org.acmsl.queryj.metadata;

/*
 * Importing QueryJ-Core classes.
 */
import org.acmsl.queryj.customsql.Result;
import org.acmsl.queryj.customsql.Sql;
import org.acmsl.queryj.metadata.vo.Attribute;
import org.acmsl.queryj.metadata.vo.ForeignKey;
import org.acmsl.queryj.metadata.vo.Row;
import org.acmsl.queryj.metadata.vo.Table;

/*
 * Importing JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing checkthread.org annotations.
 */
import org.checkthread.annotations.ThreadSafe;

/*
 * Importing JDK classes.
 */
import java.util.ArrayList;
import java.util.List;

/**
 * Partial list decorator for table attributes.
 * @author <a href="mailto:queryj@acm-sl.org">Jose San Leandro</a>
 * @since 3.0
 * Created: 2013/12/30 10:25
 */
@ThreadSafe
public class TableAttributesPartialListDecorator<T>
    implements PartialListDecorator,
               TableDecorator<T>
{
    /**
     * The table decorator.
     */
    private TableDecorator<T> m__Table;

    /**
     * The list decorator.
     */
    private ListDecorator<T> m__ListDecorator;

    /**
     * The operation types.
     */
    public static enum Operation
    {
        PLUS,
        MINUS,
        ONLY
    }

    /**
     * The operation.
     */
    private Operation m__Operation;

    /**
     * Creates a new instance.
     * @param listDecorator the {@link ListDecorator}.
     * @param table the {@link TableDecorator}.
     * @param operation the {@link Operation}.
     */
    public TableAttributesPartialListDecorator(
        @NotNull final ListDecorator<T> listDecorator,
        @NotNull final TableDecorator<T> table,
        @NotNull final Operation operation)
    {
        immutableSetListDecorator(listDecorator);
        immutableSetTable(table);
        immutableSetOperation(operation);
    }

    /**
     * Specifies the list decorator.
     * @param listDecorator such instance.
     */
    protected final void immutableSetListDecorator(@NotNull final ListDecorator<T> listDecorator)
    {
        this.m__ListDecorator = listDecorator;
    }

    /**
     * Specifies the list decorator.
     * @param listDecorator such instance.
     */
    @SuppressWarnings("unused")
    protected void setListDecorator(@NotNull final ListDecorator<T> listDecorator)
    {
        immutableSetListDecorator(listDecorator);
    }

    /**
     * Retrieves the list decorator.
     * @return such instance.
     */
    @SuppressWarnings("unused")
    public ListDecorator<T> getListDecorator()
    {
        return this.m__ListDecorator;
    }

    /**
     * Specifies the table.
     * @param table the table.
     */
    protected final void immutableSetTable(@NotNull final TableDecorator<T> table)
    {
        this.m__Table = table;
    }

    /**
     * Specifies the table.
     * @param table the table.
     */
    @SuppressWarnings("unused")
    protected void setTable(@NotNull final TableDecorator<T> table)
    {
        immutableSetTable(table);
    }

    /**
     * Retrieves the table.
     * @return such instance.
     */
    @NotNull
    public TableDecorator<T> getTable()
    {
        return this.m__Table;
    }

    /**
     * Specifies the operation.
     * @param operation such operation.
     */
    protected final void immutableSetOperation(@NotNull final Operation operation)
    {
        this.m__Operation = operation;
    }

    /**
     * Specifies the operation.
     * @param operation such operation.
     */
    @SuppressWarnings("unused")
    protected void setOperation(@NotNull final Operation operation)
    {
        immutableSetOperation(operation);
    }

    /**
     * Retrieves the operation.
     * @return such operation.
     */
    public Operation getOperation()
    {
        return this.m__Operation;
    }

    // ListDecorator implementation
    @NotNull
    public List<T> getItems()
    {
        throw new RuntimeException("Invalid operation");
    }

    @NotNull
    public PartialListDecorator plus()
    {
        throw new RuntimeException("Invalid operation");
    }

    @NotNull
    public PartialListDecorator minus()
    {
        throw new RuntimeException("Invalid operation");
    }

    // TableDecorator implementation

    @NotNull
    @Override
    public ListDecorator<T> getReadOnlyAttributes()
    {
        return getReadOnlyAttributes(getListDecorator(), getTable(), getOperation());
    }

    /**
     * An alias to make templates more readable.
     * @return the read-only attributes.
     */
    @SuppressWarnings("unused")
    @NotNull
    public ListDecorator<T> getReadOnly()
    {
        @NotNull final ListDecorator<T> result = getReadOnlyAttributes();

        return result;
    }

    /**
     * Retrieves the result of adding or removing the read-only attributes from the wrapped list.
     * @param list the list.
     * @param table the table.
     * @param operation the operation.
     * @return the resulting attributes.
     */
    @NotNull
    protected ListDecorator<T> getReadOnlyAttributes(
        @NotNull final ListDecorator<T> list,
        @NotNull final TableDecorator<T> table,
        @NotNull final Operation operation)
    {
        @NotNull final ListDecorator<T> result;

        @NotNull final List<T> aux = new ArrayList<T>(list);

        if (operation.equals(Operation.MINUS))
        {
            aux.removeAll(table.getReadOnlyAttributes());
        }
        else if (operation.equals(Operation.PLUS))
        {
            aux.addAll(table.getReadOnlyAttributes());
        }
        else
        {
            aux.retainAll(table.getReadOnlyAttributes());
        }

        result = new TableAttributesListDecorator<T>(aux, table);

        return result;
    }

    @NotNull
    @Override
    public List<Table<DecoratedString, Attribute<DecoratedString>, ListDecorator<Attribute<DecoratedString>>>>
    getAllParentTables()
    {
        return getTable().getAllParentTables();
    }

    /**
     * An alias to make templates more readable.
     * @return the externally managed attributes.
     */
    @SuppressWarnings("unused")
    @NotNull
    public ListDecorator<T> getExternallyManaged()
    {
        @NotNull final ListDecorator<T> result = getExternallyManagedAttributes();

        return result;
    }

    @NotNull
    @Override
    public ListDecorator<T> getExternallyManagedAttributes()
    {
        return getExternallyManagedAttributes(getListDecorator(), getTable(), getOperation());
    }

    /**
     * Retrieves the result of adding or removing the externally-managed attributes from the wrapped list.
     * @param list the list.
     * @param table the table.
     * @param operation the operation.
     * @return the resulting attributes.
     */
    @NotNull
    protected ListDecorator<T> getExternallyManagedAttributes(
        @NotNull final ListDecorator<T> list,
        @NotNull final TableDecorator<T> table,
        @NotNull final Operation operation)
    {
        @NotNull final ListDecorator<T> result;

        @NotNull final List<T> aux = new ArrayList<T>(list);

        if (operation.equals(Operation.MINUS))
        {
            aux.removeAll(table.getExternallyManagedAttributes());
        }
        else if (operation.equals(Operation.PLUS))
        {
            aux.addAll(table.getExternallyManagedAttributes());
        }
        else
        {
            aux.retainAll(table.getExternallyManagedAttributes());
        }

        result = new TableAttributesListDecorator<T>(aux, table);

        return result;
    }

    /**
     * Conditionally adds each element of the source, to the destination list,
     * if it's not included already.
     * @param source the source.
     * @param destination the destination.
     */
    protected void addAll(@NotNull final List<T> source, @NotNull final List<T> destination)
    {
        for (@Nullable final T item : source)
        {
            if (   (item != null)
                && (!destination.contains(item)))
            {
                destination.add(item);
            }
        }
    }

    /**
     * Conditionally removes each element of the source, from the destination list,
     * if it's included already.
     * @param source the source.
     * @param destination the destination.
     */
    protected void removeAll(@NotNull final List<T> source, @NotNull final List<T> destination)
    {
        for (@Nullable final T item : source)
        {
            if (   (item != null)
                && (destination.contains(item)))
            {
                destination.remove(item);
            }
        }
    }

    /**
     * Retrieves the dynamic queries.
     * @return such queries.
     */
    @NotNull
    @Override
    public List<Sql> getDynamicQueries()
    {
        return getTable().getDynamicQueries();
    }

    @NotNull
    @Override
    public List<Row<DecoratedString>> getStaticContent()
    {
        return getTable().getStaticContent();
    }

    @NotNull
    @Override
    public List<Result> getDifferentCustomResults()
    {
        return getTable().getDifferentCustomResults();
    }

    @NotNull
    @Override
    public List<DecoratedString> getAttributeTypes()
    {
        return getTable().getAttributeTypes();
    }

    // Table implementation
    @NotNull
    @Override
    public DecoratedString getName()
    {
        return getTable().getName();
    }

    @Nullable
    @Override
    public DecoratedString getComment()
    {
        return getTable().getComment();
    }

    @NotNull
    @Override
    public ListDecorator<Attribute<DecoratedString>> getPrimaryKey()
    {
        return getTable().getPrimaryKey();
    }

    @NotNull
    @Override
    public ListDecorator<Attribute<DecoratedString>> getAttributes()
    {
        return getTable().getAttributes();
    }

    @NotNull
    @Override
    public List<ForeignKey<DecoratedString>> getForeignKeys()
    {
        return getTable().getForeignKeys();
    }

    @Nullable
    @Override
    public Table<DecoratedString, Attribute<DecoratedString>, ListDecorator<Attribute<DecoratedString>>>
    getParentTable()
    {
        return getTable().getParentTable();
    }

    @Override
    public boolean isStatic()
    {
        return getTable().isStatic();
    }

    @Override
    public boolean isVoDecorated()
    {
        return getTable().isVoDecorated();
    }

    @Override
    public int compareTo(
        @Nullable final Table<DecoratedString, Attribute<DecoratedString>, ListDecorator<Attribute<DecoratedString>>> table)
    {
        return getTable().compareTo(table);
    }

    @NotNull
    @Override
    public String toString()
    {
        return
              "{ \"class\": \"" + TableAttributesPartialListDecorator.class.getName() + '"'
            + ", \"listDecorator\": " + m__ListDecorator
            + ", \"table\": " + m__Table
            + ", \"operation\": " + m__Operation
            + " }";
    }
}