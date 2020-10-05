/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2020, Sualeh Fatehi <sualeh@hotmail.com>.
All rights reserved.
------------------------------------------------------------------------

SchemaCrawler is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

SchemaCrawler and the accompanying materials are made available under
the terms of the Eclipse Public License v1.0, GNU General Public License
v3 or GNU Lesser General Public License v3.

You may elect to redistribute this code under any of these licenses.

The Eclipse Public License is available at:
http://www.eclipse.org/legal/epl-v10.html

The GNU General Public License v3 and the GNU Lesser General Public
License v3 are available at:
http://www.gnu.org/licenses/

========================================================================
*/
package schemacrawler.tools.executable;

import java.util.ArrayList;
import java.util.Collection;

import schemacrawler.tools.text.operation.OperationType;
import schemacrawler.tools.text.schema.SchemaTextDetailType;

public class CommandProviderUtility {

  public static Collection<CommandDescription> operationCommands() {
    final Collection<CommandDescription> supportedCommands = new ArrayList<>();
    for (final OperationType operation : OperationType.values()) {
      supportedCommands.add(new CommandDescription(operation.name(), operation.getDescription()));
    }
    return supportedCommands;
  }

  public static Collection<CommandDescription> schemaTextCommands() {
    final Collection<CommandDescription> supportedCommands = new ArrayList<>();
    for (final SchemaTextDetailType schemaTextDetailType : SchemaTextDetailType.values()) {
      supportedCommands.add(
          new CommandDescription(
              schemaTextDetailType.name(), schemaTextDetailType.getDescription()));
    }
    return supportedCommands;
  }

  private CommandProviderUtility() {
    // Prevent instantiation
  }
}
