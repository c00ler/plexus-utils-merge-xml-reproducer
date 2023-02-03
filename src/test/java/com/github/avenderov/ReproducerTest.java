package com.github.avenderov;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.Xpp3DomUtils;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReproducerTest {

    public static final String PARENT = "" +
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

    public static final String CHILD = "" +
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

    public static final String EXPECTED_RESULT = "" +
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

    @Test
    void shouldMergeConfigurationsUsingXpp3DomUtils() throws Exception {
        var parentDom = toXpp3Dom(PARENT);
        var childDom = toXpp3Dom(CHILD);
        var expectedDom = toXpp3Dom(EXPECTED_RESULT);

        var resultDom = Xpp3DomUtils.mergeXpp3Dom(parentDom, childDom);

        assertEquals(expectedDom, resultDom);
    }

    @Test
    void shouldMergeConfigurationsUsingXpp3Dom() throws Exception {
        var parentDom = toXpp3Dom(PARENT);
        var childDom = toXpp3Dom(CHILD);
        var expectedDom = toXpp3Dom(EXPECTED_RESULT);

        // The order of arguments is different
        var resultDom = Xpp3Dom.mergeXpp3Dom(childDom, parentDom);

        assertEquals(expectedDom, resultDom);
    }

    private static Xpp3Dom toXpp3Dom(String xml) throws Exception {
        return Xpp3DomBuilder.build(new StringReader(xml));
    }
}
