/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.sql;

import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorScope;
import org.apache.calcite.util.ImmutableNullableList;

import java.util.List;

/**
 * A <code>SqlDropQuark</code> is a node of a parse tree which represents a DROP DDL
 * statements for Quark.
 */
public class SqlDropQuark extends SqlCall {
  public static final SqlSpecialOperator OPERATOR =
      new SqlSpecialOperator("DROP_QUARK", SqlKind.OTHER_DDL);

  SqlNode condition;

  //~ Constructors -----------------------------------------------------------

  public SqlDropQuark(
      SqlParserPos pos,
      SqlNode condition) {
    super(pos);
    this.condition = condition;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public SqlKind getKind() {
    return SqlKind.OTHER_DDL;
  }

  public SqlOperator getOperator() {
    return OPERATOR;
  }

  public List<SqlNode> getOperandList() {
    return ImmutableNullableList.of(condition);
  }

  @Override public void setOperand(int i, SqlNode operand) {
    switch (i) {
    case 0:
      condition = operand;
      break;
    default:
      throw new AssertionError(i);
    }
  }

  /**
   * Gets the filter condition for rows to be deleted.
   *
   * @return the condition expression for the data to be deleted, or null for
   * all rows in the table
   */
  public SqlNode getCondition() {
    return condition;
  }

  @Override public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
    final SqlWriter.Frame frame =
        writer.startList(SqlWriter.FrameTypeEnum.SELECT, "DROP DATASOURCE", "");
    final int opLeft = getOperator().getLeftPrec();
    final int opRight = getOperator().getRightPrec();
    if (condition != null) {
      writer.sep("WHERE");
      condition.unparse(writer, opLeft, opRight);
    }
    writer.endList(frame);
  }

  public void validate(SqlValidator validator, SqlValidatorScope scope) {
    throw new UnsupportedOperationException("Validation not supported for Quark's DDL");
  }
}

// End SqlDropQuark.java
