package <%= props.processPackage %>;

import br.com.martinlabs.commons.DaoUnitTestWrapper;
import br.com.martinlabs.commons.DaoWrapper;
import <%= props.modelPackage %>.<%= table.className %>;<% 
var antiRepeat = [];
for (var i in table.NtoNcolumns) { 
    var cn = table.NtoNcolumns[i]; 
    if (cn != null && antiRepeat.indexOf(cn.otherTable.className) < 0) {
        antiRepeat.push(cn.otherTable.className);
%>
import <%= props.modelPackage %>.<%= cn.otherTable.className %>;<%
    }
}
%>
import <%= props.responsePackage %>.<%= table.className %>Resp;
import br.com.martinlabs.commons.PagedResp;
import br.com.martinlabs.commons.SecurityUtils;
import br.com.martinlabs.commons.exceptions.RespException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author martinlabs CRUD generator
 */
public class <%= table.className %>ProcessTest extends DaoUnitTestWrapper {

    private Connection con;
    private LoginServices loginS;
    private <%= table.className %>Process subject;

    public <%= table.className %>ProcessTest() throws NamingException, SQLException {
        super("<%= props.datasource %>", "<%= props.database %>");
        con = getConnection();
        subject = new <%= table.className %>Process(con);
        loginS = new LoginServices(con);
    }

    @Test(expected = RespException.class)
    public void testListPageNull() {
        String token = loginS.loginToToken("user@gmail.com", SecurityUtils.sha1("abcabc"));
        String query = null;
        Integer page = null;
        Integer limit = 20;
        String orderRequest = null;
        Boolean asc = null;
                
        subject.list(token, query, page, limit, orderRequest, asc);
    }

    @Test(expected = RespException.class)
    public void testListLimitNull() {
        String token = loginS.loginToToken("user@gmail.com", SecurityUtils.sha1("abcabc"));
        String query = null;
        Integer page = 0;
        Integer limit = null;
        String orderRequest = null;
        Boolean asc = null;
                
        subject.list(token, query, page, limit, orderRequest, asc);
    }

    @Test
    public void testListNoQuery() {
        String token = loginS.loginToToken("user@gmail.com", SecurityUtils.sha1("abcabc"));
        String query = null;
        Integer page = 0;
        Integer limit = 20;
        String orderRequest = null;
        Boolean asc = null;
                
        PagedResp<<%= table.className %>> result = subject.list(token, query, page, limit, orderRequest, asc);
        assertNotNull(result);
        assertNotNull(result.getList());
        assertNotEquals(result.getCount(), 0);
        assertFalse(result.getList().isEmpty());
        assertNotNull(result.getList().get(0));
    }

    @Test
    public void testListWithQuery() {
        String token = loginS.loginToToken("user@gmail.com", SecurityUtils.sha1("abcabc"));<%

var existPureStringColumn = false;
for (var i in table.columns) {
    var c = table.columns[i];
    if (c.javaType === "String" && !c.smartType) {
        existPureStringColumn = true;
        break;
    }
}
if (existPureStringColumn) {
%>
        String query = "lorem";<% 
} else { 
%>
        String query = "1";<% 
} 
%>
        Integer page = 0;
        Integer limit = 20;
        String orderRequest = null;
        Boolean asc = null;
                
        PagedResp<<%= table.className %>> result = subject.list(token, query, page, limit, orderRequest, asc);
        assertNotNull(result);
        assertNotNull(result.getList());
        assertNotEquals(result.getCount(), 0);
        assertFalse(result.getList().isEmpty());
        assertNotNull(result.getList().get(0));
    }

    @Test
    public void testGet() {
        String token = loginS.loginToToken("user@gmail.com", SecurityUtils.sha1("abcabc"));

        <%= table.className %>Resp result = subject.get(<% 
for (var k in table.primaryColumns) {
    %>1, <%
}
            %> token);
        assertNotNull(result);
        assertNotNull(result.get<%= table.className %>());<%
antiRepeat = [];
for (var j in table.columns) { 
    var cx = table.columns[j]; 
    if (cx.referencedTable && antiRepeat.indexOf(cx.referencedTable.className) < 0) {
        antiRepeat.push(cx.referencedTable.className);
%>
        assertFalse(result.getAll<%= cx.referencedTable.className %>().isEmpty());
        assertNotNull(result.getAll<%= cx.referencedTable.className %>().get(0));<%
    }
}

antiRepeat = [];
for (var j in table.NtoNcolumns) { 
    var cx = table.NtoNcolumns[j]; 
    if (antiRepeat.indexOf(cx.otherTable.className) < 0) {
        antiRepeat.push(cx.otherTable.className);
%>
        assertFalse(result.getAll<%= cx.otherTable.className %>().isEmpty());
        assertNotNull(result.getAll<%= cx.otherTable.className %>().get(0));
<%
    }
}
%>
    }
<%
for (var i in table.columns) { 
    var c = table.columns[i];

    if (c.column_key === "UNI") {
%>
    @Test(expected = RespException.class)
    public void testPersistWithRepeated<%= c.propertyNameUpper %>() {
        String token = loginS.loginToToken("user@gmail.com", SecurityUtils.sha1("abcabc"));
        <%= table.className %> <%= table.classLowerCamel %> = new <%= table.className %>(); <%
        for (var j in table.columns) { 
            var cx = table.columns[j]; 
            if (cx.is_nullable === "NO" && (cx.column_key != "PRI" || cx.referencedTable) && cx.column_name != c.column_name) { 
                if (["double", "long"].indexOf(cx.javaType) > -1) {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>(1);<% 
                } else if (cx.javaType === "String") {
                    if (cx.smartType === "email") {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>("any@email.com");<% 
                    } else {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>("X");<% 
                    }
                } else if (cx.javaType === "Date") {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>(new Date());<% 
                }
            } 
        }


        if (["double", "long"].indexOf(c.javaType) > -1) {
%>
        <%= table.classLowerCamel %>.set<%= c.propertyNameUpper %>(1);<% 
        } else if (c.javaType === "String") {
            if (c.smartType === "email") {
%>
        <%= table.classLowerCamel %>.set<%= c.propertyNameUpper %>("lorem@email.com");<% 
            } else {
%>
        <%= table.classLowerCamel %>.set<%= c.propertyNameUpper %>("lorem");<% 
            }
        }

%>
        
        subject.persist(<%= table.classLowerCamel %>, token);
    }
<%
    }
}
%>
    @Test
    public void testPersist() {
        String token = loginS.loginToToken("user@gmail.com", SecurityUtils.sha1("abcabc"));
        <%= table.className %> <%= table.classLowerCamel %> = new <%= table.className %>(); <%
for (var j in table.columns) { 
    var cx = table.columns[j]; 
    if (cx.is_nullable === "NO" && (cx.column_key != "PRI" || cx.referencedTable)) { 
        if (["double", "long"].indexOf(cx.javaType) > -1) {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>(1);<% 
        } else if (cx.javaType === "String") {
            if (cx.smartType === "email") {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>("any@email.com");<% 
            } else {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>("X");<% 
            }
        } else if (cx.javaType === "Date") {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>(new Date());<% 
        }
    } 
} 
%>
        
        Long result = subject.persist(<%= table.classLowerCamel %>, token);
        assertNotNull(result);
        assertTrue(result > 0);
    }
<% 

for (var i in table.NtoNcolumns) { 
    var col = table.NtoNcolumns[i]; 
%>
    @Test
    public void testPersistWith<%= col.NtoNtable.className %>() {
        String token = loginS.loginToToken("user@gmail.com", SecurityUtils.sha1("abcabc"));
        <%= table.className %> <%= table.classLowerCamel %> = new <%= table.className %>(); <%
    for (var j in table.columns) { 
        var cx = table.columns[j]; 
        if (cx.is_nullable === "NO" && (cx.column_key != "PRI" || cx.referencedTable)) { 
            if (["double", "long"].indexOf(cx.javaType) > -1) {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>(1);<% 
            } else if (cx.javaType === "String") {
                if (cx.smartType === "email") {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>("any@email.com");<% 
                } else {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>("X");<% 
                }
            } else if (cx.javaType === "Date") {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>(new Date());<% 
            }
        } 
    } 
%>
        <%= table.classLowerCamel %>.set<%= col.NtoNtable.className %>(new ArrayList<>());
        <%= col.otherTable.className %> <%= col.otherTable.classLowerCamel %> = new <%= col.otherTable.className %>();
        <%= col.otherTable.classLowerCamel %>.set<%= col.otherTable.primaryColumns[0].propertyNameUpper %>(1);
        <%= table.classLowerCamel %>.get<%= col.NtoNtable.className %>().add(<%= col.otherTable.classLowerCamel %>);
        
        Long result = subject.persist(<%= table.classLowerCamel %>, token);
        assertNotNull(result);
        assertTrue(result > 0);
    }
<% 
} 

if (!table.primaryColumns[0].referencedTable) { 
%>
    @Test
    public void testPersistUpdating() {
        String token = loginS.loginToToken("user@gmail.com", SecurityUtils.sha1("abcabc"));
        <%= table.className %> <%= table.classLowerCamel %> = new <%= table.className %>(); 
        <%= table.classLowerCamel %>.set<%= table.primaryColumns[0].propertyNameUpper %>(1);<%
    for (var j in table.columns) { 
        var cx = table.columns[j]; 
        if (cx.is_nullable === "NO" && cx.column_key != "PRI") { 
            if (["double", "long"].indexOf(cx.javaType) > -1) {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>(1);<% 
            } else if (cx.javaType === "String") {
                if (cx.smartType === "email") {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>("any@email.com");<% 
                } else {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>("X");<% 
                }
            } else if (cx.javaType === "Date") {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>(new Date());<% 
            }
        } 
    } 
%>
        
        Long result = subject.persist(<%= table.classLowerCamel %>, token);
        assertNotNull(result);
        assertTrue(result > 0);
    }
<% 
} else { 
%>
    @Test
    public void testPersistInserting() {
        String token = loginS.loginToToken("user@gmail.com", SecurityUtils.sha1("abcabc"));
        <%= table.className %> <%= table.classLowerCamel %> = new <%= table.className %>();
<% 
    for (var i in table.primaryColumns) {
        var primaryCol = table.primaryColumns[i];
%>
        long <%= primaryCol.propertyName %> = DaoWrapper.updateForTest(con, "INSERT INTO <%= primaryCol.referencedTable.name %> ( "<% 
        var colocarVirgula = false;
        for (var i in primaryCol.referencedTable.columns) { 
            var c = primaryCol.referencedTable.columns[i]; 
            if (c.extra !== "auto_increment" && c.is_nullable === "NO") { 
%>
            + "<%= colocarVirgula ? ',' : '' %><%= c.column_name %> "<% 
                colocarVirgula = true;
            }
        } 
        if (!colocarVirgula) {
            for (var i in primaryCol.referencedTable.columns) { 
                var c = primaryCol.referencedTable.columns[i]; 
                if (c.extra !== "auto_increment") { 
%>
            + "<%= c.column_name %> "<% 
                    break;
                }
            } 
        }
        colocarVirgula = false;
%>
            + ") VALUES ( "
            + "<% 
        for (var i in primaryCol.referencedTable.columns) { 
            var c = primaryCol.referencedTable.columns[i]; 
            if (c.extra !== 'auto_increment' && c.is_nullable === 'NO') {
                %><%= colocarVirgula ? ',' : '' %>?<% 
                colocarVirgula = true;
            }
        }
        if (!colocarVirgula) {
            %>?<%
        }

         %>"
            + ") ",<% 
        colocarVirgula = false;
        for (var i in primaryCol.referencedTable.columns) { 
            var c = primaryCol.referencedTable.columns[i]; 
            if (c.extra !== "auto_increment" && c.is_nullable === "NO") { %><%= colocarVirgula ? ',' : '' %><% 
                if (["double", "long", "boolean"].indexOf(c.javaType) > -1) { 
%>
            1<% 
                } else if (c.javaType === "String") { 
%>
            "X"<% 
                } else if (c.javaType === "Date") { 
%>
            new Date()<% 
                }
                colocarVirgula = true;
            }
        } 
        if (!colocarVirgula) {
            for (var i in primaryCol.referencedTable.columns) { 
                var c = primaryCol.referencedTable.columns[i]; 
                if (c.extra !== "auto_increment") {
                    if (["double", "long", "boolean"].indexOf(c.javaType) > -1) { 
%>
            1<% 
                    } else if (c.javaType === "String") { 
%>
            "X"<% 
                    } else if (c.javaType === "Date") { 
%>
            new Date()<% 
                    }
                    break;
                }
            }
        }

        %>).key;
         
        <%= table.classLowerCamel %>.set<%= primaryCol.propertyNameUpper %>(<%= primaryCol.propertyName %>);
<%
    }

    for (var j in table.columns) { 
        var cx = table.columns[j]; 
        if (cx.is_nullable === "NO" && cx.column_key != "PRI") { 
            if (["double", "long"].indexOf(cx.javaType) > -1) {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>(1);<% 
            } else if (cx.javaType === "String") {
                if (cx.smartType === "email") {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>("any@email.com");<% 
                } else {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>("X");<% 
                }
            } else if (cx.javaType === "Date") {
%>
        <%= table.classLowerCamel %>.set<%= cx.propertyNameUpper %>(new Date());<% 
            }
        } 
    } 
%>
        
        Long result = subject.persist(<%= table.classLowerCamel %>, token);
        assertNotNull(result);
        assertTrue(result > 0);
    }
<% 
} 
%>
}