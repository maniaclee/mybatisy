package com.lvbby.sqler.factory;

import com.google.common.collect.Lists;
import com.lvbby.sqler.core.TableFactory;
import com.lvbby.sqler.core.TableField;
import com.lvbby.sqler.core.TableInfo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by peng on 16/7/28.
 */
public class DDLTableFactory implements TableFactory {
    static Pattern p_table = Pattern.compile("\\s*CREATE\\s+TABLE\\s+`(?<table>\\S+)`\\s*\\((?<body>[^;]+);");
    static Pattern p_sentence = Pattern.compile("\\s*`(?<field>\\S+)`\\s+(?<type>[^\\(\\)\\s]+)[^,]+,");
    private String ddl;

    public static DDLTableFactory create(String ddl) {
        DDLTableFactory re = new DDLTableFactory();
        re.ddl = ddl;
        return re;
    }

    @Override
    public List<TableInfo> getTables() {
        List<TableInfo> re = Lists.newArrayList();
        for (Matcher matcher = p_table.matcher(ddl); matcher.find(); ) {
            TableInfo tableInfo = new TableInfo();
            tableInfo.setName(matcher.group("table"));
            String body = matcher.group("body");
            for (Matcher m = p_sentence.matcher(body); m.find(); ) {
                TableField tableField = new TableField();
                tableField.setName(m.group("field"));
                tableField.setDbTypeName(m.group("type"));
                tableInfo.getFields().add(tableField);
            }
            re.add(tableInfo);
        }
        return re;
    }

}