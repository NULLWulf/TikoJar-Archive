package com.tikoJar.DTO;

import java.time.LocalDate;

public interface ProjectionTemplates extends ENDPOINT {

    String defaultEmpty = "{\"document\":null}";

    String found1Updated1 = """
                            {
                            "matchedCount" : 1,
                            "modifiedCount" : 1
                            }""".stripIndent();

    String checkJarExistsQuery = """
               {"collection": "Jars",
               "database": "TikoJarTest",
               "dataSource": "PositivityJar",
               "filter": {
                   "serverID": "%s"
               },
               "update": {
                   "$pull": {"messages":{"messageId":"%s"}}}}}
                """.formatted(serverId, messageId);

    String checkJarExistsQuery = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar"}
                """;

    String checkAndReturnExpired = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "openingCondition.hasMessageLimit": { "$eq" : false },
                            "openingCondition.openingDate": { "$eq" : "%s" }}}
                """.formatted(LocalDate.now().toString());

    String checkJarExistsQuery = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "serverID": "%s" }}
                """.formatted(serverId);

    String checkJarExistsQuery = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "serverID": "%s" }}
                """.formatted(serverId);

    String addMessageQuery = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "serverID": "%s" },
                "update": {
                    "$push": {"messages": %s}}}
                """.formatted(serverId, new JSON_Handler().getObjAsJSONString(addMessage).stripIndent());  // converts newMessage to JSON format

    String createJarQuery = """
                {"collection":"Jars",
                    "database":"TikoJarTest",
                    "dataSource":"PositivityJar",
                    "document": %s}
                """.formatted(new JSON_Handler().getObjAsJSONString(jar).stripIndent());

    String pullJar = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "serverID": "%s" }}}
                """.formatted(serverId);

}
