//;-*- mode: antlr -*-
/*
                        QueryJ

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
    Contact info: chous@acm-sl.org
    Postal Address: c/Playa de Lagoa, 1
                    Urb. Valdecabanas
                    Boadilla del monte
                    28660 Madrid
                    Spain

 ******************************************************************************
 *
 * (From http://svn.acm-sl.org/queryj/branches/br-0_6--ventura24-2_0-0/src/main/antlr/PerComment.g)
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Defines the ANTLR parser rules for physical e/r comments.
 *
 */
grammar PerComment;

@parser::header
{
/*
                        QueryJ

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
    Contact info: chous@acm-sl.org
    Postal Address: c/Playa de Lagoa, 1
                    Urb. Valdecabanas
                    Boadilla del monte
                    28660 Madrid
                    Spain

 ******************************************************************************
 *
 * Generated from PerComment.g by ANTLR.
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: ANTLR parser for PerComment.g
 *
 */
package org.acmsl.queryj.tools.antlr;

/* 
 * Importing some JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing some JDK classes.
 */
import java.util.List;
}

@lexer::header
{
/*
                        QueryJ

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
    Contact info: chous@acm-sl.org
    Postal Address: c/Playa de Lagoa, 1
                    Urb. Valdecabanas
                    Boadilla del monte
                    28660 Madrid
                    Spain

 ******************************************************************************
 *
 * Generated from PerComment.g by ANTLR.
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: ANTLR lexer for PerComment.g
 *
 */
package org.acmsl.queryj.tools.antlr;
}

@parser::members
{
/**
 * The table comment.
 */
private String tableComment;

/**
 * The table static.
 */
private String tableStatic;

/**
 * The table ISA.
 */
private String tableIsa;

/**
 * The table ISA-type.
 */
private String tableIsaType;

/**
 * The table decorator attribute.
 */
private boolean tableDecorator;

/**
 * The table relationship attribute.
 */
private List<List<String>> tableRelationship;

/**
 * The column comment.
 */
private String columnComment;

/**
 * The column boolean <code>true</code> values.
 */
private String columnBoolTrue;

/**
 * The column boolean <code>false</code> values.
 */
private String columnBoolFalse;

/**
 * The column boolean <code>null</code> values.
 */
private String columnBoolNull;

/**
 * Whether the column is readonly.
 */
private boolean columnReadOnly = false;

/**
 * The column isa-ref mappings.
 */
private List<List<String>> columnIsaRefs;

/**
 * The column oraseq attribute.
 */
private String columnOraSeq;

/**
 * Specifies the table comment.
 * @param comment such comment.
 */
protected void setTableComment(@NotNull final String comment)
{
    tableComment = trim(comment);
}

/**
 * Retrieves the table comment.
 * @return such information.
 */
@Nullable
public String getTableComment()
{
    return tableComment;
}

/**
 * Specifies the column name used to distinguish the
 * static content.
 * @param name such name.
 */
protected void setTableStatic(@NotNull final String name)
{
    tableStatic = trim(name);
}

/**
 * Retrieves the column name used to distinguish the
 * static content.
 * @return such column name.
 */
@Nullable
public String getTableStatic()
{
    return tableStatic;
}

/**
 * Specifies the table name defined as parent table.
 * @param name such name.
 */
protected void setTableIsa(@NotNull final String name)
{
    tableIsa = trim(name);
}

/**
 * Retrieves the table name defined as parent table.
 * @return such table name.
 */
@Nullable
public String getTableIsa()
{
    return tableIsa;
}

/**
 * Specifies the table name whose descendants get identified
 * by the contents of this table.
 * @param name such name.
 */
protected void setTableIsaType(@NotNull final String name)
{
    tableIsaType = trim(name);
}

/**
 * Retrieves the table name whose descendants get identified
 * by the contents of this table.
 * @return such table name.
 */
@Nullable
public String getTableIsaType()
{
    return tableIsaType;
}

/**
 * Specifies whether the table-specific value-object will be wrapped
 * by custom decorators.
 * @param flag such flag.
 */
protected void setTableDecorator(final boolean flag)
{
    tableDecorator = flag;
}

/**
 * Retrieves whether the table-specific value-object will be wrapped
 * by custom decorators.
 * @return such information.
 */
public boolean getTableDecorator()
{
    return tableDecorator;
}

/**
 * Specifies the relationship this table models.
 * @param relationship such content.
 */
protected void setTableRelationship(final List<List<String>> relationship)
{
    tableRelationship = relationship;
}

/**
 * Retrieves whether the table models a relationship.
 * @return such information.
 */
@NotNull
public List<List<String>> getTableRelationship()
{
    List<List<String>> result = tableRelationship;

    if (result == null)
    {
        result = new ArrayList<List<String>>(0);
    }

    return result;
}

/**
 * Specifies the column comment.
 * @param comment such comment.
 */
protected void setColumnComment(@NotNull final String comment)
{
    columnComment = trim(comment);
}

/**
 * Retrieves the column comment.
 * @return such information.
 */
@Nullable
public String getColumnComment()
{
    return columnComment;
}

/**
 * Specifies the value used as <code>true</code> for boolean attributes.
 * and how it denotes a <code>true</code> value.
 * @param value the value denoting <code>true</code> values.
 */
protected void setColumnBoolTrue(@NotNull final String value)
{
    columnBoolTrue = value;
}

/**
 * Retrieves the value used as <code>true</code> for boolean attributes.
 * and how it denotes a <code>true</code> value.
 * @return  the <code>true</code> value, or null if
 * the column is not defined as boolean.
 */
@Nullable
public String getColumnBoolTrue()
{
    return columnBoolTrue;
}

/**
 * Specifies the value used as <code>false</code> for boolean attributes.
 * and how it denotes a <code>false</code> value.
 * @param value the value denoting <code>false</code> values.
 */
protected void setColumnBoolFalse(@NotNull final String value)
{
    columnBoolFalse = value;
}

/**
 * Retrieves the value used as <code>false</code> for boolean attributes.
 * and how it denotes a <code>false</code> value.
 * @return  the <code>false</code> value, or null if
 * the column is not defined as boolean.
 */
@Nullable
public String getColumnBoolFalse()
{
    return columnBoolFalse;
}

/**
 * Specifies the value used as <code>null</code> for boolean attributes.
 * and how it denotes a <code>null</code> value.
 * @param value the value denoting <code>null</code> values.
 */
protected void setColumnBoolNull(@NotNull final String value)
{
    columnBoolNull = value;
}

/**
 * Retrieves the value used as <code>null</code> for boolean attributes.
 * and how it denotes a <code>null</code> value.
 * @return  the <code>null</code> value, or null if
 * the column is not defined as boolean.
 */
@Nullable
public String getColumnBoolNull()
{
    return columnBoolNull;
}

/**
 * Specifies whether the column is declared as read-only or not.
 * @param flag such flag.
 */
protected void setColumnReadOnly(final boolean flag)
{
    columnReadOnly = flag;
}

/**
 * Retrieves whether the column is declared as read-only or not.
 * @return such information.
 */
public boolean getColumnReadOnly()
{
    return columnReadOnly;
}

/**
 * Specifies the associations between column values and
 * table names (ISA implementations).
 * @param mappings the mappings.
 */
protected void setColumnIsaRefs(@NotNull final List<List<String>> mappings)
{
    columnIsaRefs = mappings;
}

/**
 * Retrieves the associations between column values and
 * table names (ISA implementations).
 * @return such mappings.
 */
@NotNull
public List<List<String>> getColumnIsaRefs()
{
    List<List<String>> result = columnIsaRefs;

    if (result == null)
    {
        result = new ArrayList<List<String>>(0);
    }

    return result;
}

/**
 * Specifies the 'oraseq' attribute.
 * @param value such value.
 */
protected void setColumnOraSeq(@NotNull final String value)
{
    columnOraSeq = trim(value);
}

/**
 * Retrieves the 'oraseq' attribute.
 * @return such information.
 */
@Nullable
public String getColumnOraSeq()
{
    return columnOraSeq;
}

/**
 * Trims given value.
 * @param value the value.
 */
@Nullable
protected String trim(@Nullable final String value)
{
    @Nullable String result = value;

    if  (result != null)
    {
        result = result.trim();
    }

    return result;
}

/**
 * Called when a token mismatch occurs.
 * We override it to raise the exception,
 * rather than recovering, on mismatched token within alt.
 * @param input the input.
 * @param type the type.
 * @param follow whatever it means in ANTLR engine.
 * @throws RecognitionException always.
 */
protected void mismatch(
    @NotNull final IntStream input, final int type, final BitSet follow)
  throws RecognitionException
{
    throw new MismatchedTokenException(type, input);
}
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

tableComment : (t=text)? ( tab_annotation )* { setTableComment(t); };
        
columnComment : (t=text)? ( col_annotation )* { setColumnComment(t); };

text returns [String result]
@init { result = null; StringBuffer aux = new StringBuffer(); }
  : (  t=text_or_id  { aux.append($t.text); }
     | c=COMMA { aux.append($c.text); }
     | WS
     | OPEN_PAREN
     | CLOSE_PAREN
     | QUOTE )+
    { result = aux.toString(); }
  ;
        
tab_annotation
  : (
        s=tab_static       { setTableStatic(s); }
      | i=tab_isa          { setTableIsa(i); }
      | t=tab_isatype      { setTableIsaType(t); }
      |   tab_decorator    { setTableDecorator(true); }
      |   tab_relationship
    )
  ;

tab_static returns [String result]
@init { result = null; }
  : STATIC WS i=ident WS? { result = $i.text; }
  ;

tab_isa returns [String result]
@init { result = null; }
  : ISA WS i=ident WS? { result = $i.text; }
  ;

tab_isatype returns [String result]
@init { result = null; }
  : ISATYPE WS i=ident WS? { result = $i.text; }
  ;

tab_decorator :  DECORATOR WS?;

tab_relationship
@init
{
    List<List<String>> contents = new ArrayList<List<String>>();
    List<String> aux;
    String first = null;
    String second = null;
}
  :  RELATIONSHIP
     WS
     (
       OPEN_PAREN WS?
       (  (SQUOTE a=text_or_id SQUOTE) { first = $a.text; }
        | (DQUOTE b=text_or_id DQUOTE) { first = $b.text; }
        | (c=text_or_id) { first = $c.text; })
       WS? COMMA WS?
       (  (SQUOTE d=ID SQUOTE) { second = $d.text; }
        | (DQUOTE e=ID DQUOTE) { second = $e.text; }
        | (f=ID) { second = $f.text; })
       WS? CLOSE_PAREN WS? COMMA?
       {
           first = trim(first);
           second = trim(second);

           if (   (first != null)
               && (second != null))
           {
               aux = new ArrayList<String>(2);
               aux.add(first);
               aux.add(second);
               contents.add(aux);
           }
       }
     )+
     {
         setTableRelationship(contents);
     }
  ;

col_annotation
  : (
         col_readonly { setColumnReadOnly(true); }
     |   col_bool
     |   col_isarefs
     | s=col_oraseq   { setColumnOraSeq(s); }
    );

col_readonly :  READONLY WS?;

col_bool
  : BOOL WS
    i=ident
    {
        setColumnBoolTrue($i.text);
    }
    WS? COMMA WS?
    j=ident { setColumnBoolFalse($j.text); }
    WS? (COMMA WS? k=ident WS? { setColumnBoolNull($k.text); })?
  ;

text_or_id 
    :    ID
    |     TEXT
    ;

ident returns [String tet]
@init {txt = null;}
    :   (SQUOTE i=ID SQUOTE) {text = ($i.text);}
    |   (DQUOTE j=ID DQUOTE) {text = ($j.text);}
    | k=ID {text = $k.text;}
    ;

col_isarefs
@init
{
    List<List<String>> contents = new ArrayList<List<String>>();
    List<String> aux;
    String first = null;
    String second = null;
}
  :  ISAREFS
     WS
     (
       OPEN_PAREN WS?
       (  (SQUOTE a=text_or_id SQUOTE) { first = $a.text; }
        | (DQUOTE b=text_or_id DQUOTE) { first = $b.text; }
        | (c=text_or_id) { first = $c.text; })
       WS? COMMA WS?
       (  (SQUOTE d=ID SQUOTE) { second = $d.text; }
        | (DQUOTE e=ID DQUOTE) { second = $e.text; }
        | (f=ID) { second = $f.text; })
       WS? CLOSE_PAREN WS? COMMA?
       {
           first = trim(first);
           second = trim(second);
           if (   (first != null)
               && (second != null))
           {
               aux = new ArrayList<String>(2);
               aux.add(first);
               aux.add(second);
               contents.add(aux);
           }
       }
     )+
     {
         setColumnIsaRefs(contents);
     }
  ;

col_oraseq returns [String result]
@init { result = null; }
  : ORASEQ WS i=ident WS? { result = $i.text; }
  ;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

AT
    :  (  STATIC
        | ISA
        | ISATYPE
        | DECORATOR
        | ISAREFS
        | READONLY
        | BOOL
        | ORASEQ
        | RELATIONSHIP
        | '@')
    ;

SQUOTE : ('\'');
DQUOTE : ('"');
fragment QUOTE : (SQUOTE | DQUOTE);

// literals
OPEN_PAREN : '(';
CLOSE_PAREN : ')';
COMMA : ',';

// keywords
fragment STATIC : '@static';
fragment ISA : '@isa';
fragment ISATYPE : '@isatype';
fragment DECORATOR : '@decorator';
fragment ISAREFS : '@isarefs';
fragment READONLY : '@readonly';
fragment RELATIONSHIP : '@relationship';
fragment BOOL : '@bool';
fragment ORASEQ : '@oraseq';

WS : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+;// {$channel = HIDDEN;};

ID
    : (LETTER | '_' | DIGIT ) (NAMECHAR)* WS?
    ;

TEXT
   : (  ID
      | WS
      | OPEN_PAREN
      | CLOSE_PAREN
      | COMMA
      | QUOTE
      | AT
      | (~('@'|','|'('|')'|'\''|'"')+))
   ;

fragment NAMECHAR
    : LETTER | DIGIT | '.' | '-' | '_' | ':' | '$'
    ;

fragment DIGIT
    :    '0'..'9'
    ;

fragment LETTER
    : 'a'..'z'
    | 'A'..'Z'
    ;