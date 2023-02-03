package com.github.avenderov;

import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.Xpp3DomUtils;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReproducerTest {

    @Test
    void shouldMergeConfigurations() throws Exception {
        var lhs = "" +
            "<configuration>\n" +
            "    <gradleEnterprise>\n" +
            "        <plugins>\n" +
            "            <plugin>\n" +
            "                <groupId>org.apache.maven.plugins</groupId>\n" +
            "                <artifactId>maven-compiler-plugin</artifactId>\n" +
            "                <executions>\n" +
            "                    <execution>\n" +
            "                        <id>default-compile</id>\n" +
            "                        <outputs>\n" +
            "                            <notCacheableBecause>something</notCacheableBecause>\n" +
            "                        </outputs>\n" +
            "                    </execution>\n" +
            "                </executions>\n" +
            "            </plugin>\n" +
            "            <plugin>\n" +
            "                <groupId>org.apache.maven.plugins</groupId>\n" +
            "                <artifactId>maven-surefire-plugin</artifactId>\n" +
            "                <inputs>\n" +
            "                    <properties>\n" +
            "                        <property>\n" +
            "                            <name>additionalProperty</name>\n" +
            "                            <value>parent-additionalProperty</value>\n" +
            "                        </property>\n" +
            "                    </properties>\n" +
            "                </inputs>\n" +
            "            </plugin>\n" +
            "        </plugins>\n" +
            "    </gradleEnterprise>\n" +
            "</configuration>\n";

        var rhs = "" +
            "<configuration>\n" +
            "    <gradleEnterprise>\n" +
            "        <plugins>\n" +
            "            <plugin>\n" +
            "                <groupId>org.apache.maven.plugins</groupId>\n" +
            "                <artifactId>maven-compiler-plugin</artifactId>\n" +
            "            </plugin>\n" +
            "            <plugin>\n" +
            "                <groupId>org.apache.maven.plugins</groupId>\n" +
            "                <artifactId>maven-surefire-plugin</artifactId>\n" +
            "                <inputs>\n" +
            "                    <properties combine.children=\"append\">\n" +
            "                        <property>\n" +
            "                            <name>childAdditionalProperty</name>\n" +
            "                            <value>child-additionalProperty</value>\n" +
            "                        </property>\n" +
            "                    </properties>\n" +
            "                </inputs>\n" +
            "            </plugin>\n" +
            "        </plugins>\n" +
            "    </gradleEnterprise>\n" +
            "</configuration>";

        var leftDom = Xpp3DomBuilder.build(new StringReader(lhs));
        var rightDom = Xpp3DomBuilder.build(new StringReader(rhs));

        var resultDom = Xpp3DomUtils.mergeXpp3Dom(leftDom, rightDom, null);

        var expectedResult = "" +
            "<configuration>\n" +
            "  <gradleEnterprise>\n" +
            "    <plugins>\n" +
            "      <plugin>\n" +
            "        <groupId>org.apache.maven.plugins</groupId>\n" +
            "        <artifactId>maven-compiler-plugin</artifactId>\n" +
            "        <executions>\n" +
            "          <execution>\n" +
            "            <id>default-compile</id>\n" +
            "            <outputs>\n" +
            "              <notCacheableBecause>something</notCacheableBecause>\n" +
            "            </outputs>\n" +
            "          </execution>\n" +
            "        </executions>\n" +
            "      </plugin>\n" +
            "      <plugin>\n" +
            "        <groupId>org.apache.maven.plugins</groupId>\n" +
            "        <artifactId>maven-surefire-plugin</artifactId>\n" +
            "        <inputs>\n" +
            "          <properties combine.children=\"append\">\n" +
            "            <property>\n" +
            "              <name>additionalProperty</name>\n" +
            "              <value>parent-additionalProperty</value>\n" +
            "            </property>\n" +
            "            <property>\n" +
            "              <name>childAdditionalProperty</name>\n" +
            "              <value>child-additionalProperty</value>\n" +
            "            </property>\n" +
            "          </properties>\n" +
            "        </inputs>\n" +
            "      </plugin>\n" +
            "    </plugins>\n" +
            "  </gradleEnterprise>\n" +
            "</configuration>\n";

        assertEquals(Xpp3DomBuilder.build(new StringReader(expectedResult)), resultDom);
    }
}
