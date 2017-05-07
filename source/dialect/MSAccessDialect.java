package dialect;

import java.sql.Types;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;

public class MSAccessDialect extends Dialect {

	public MSAccessDialect() {
                super();
                registerHibernateType(Types.BIT, "BIT" ); 
                registerHibernateType( Types.BIGINT, "INTEGER" );
                registerHibernateType( Types.SMALLINT, "SMALLINT" );
                registerHibernateType( Types.TINYINT, "TINYINT" );
                registerHibernateType( Types.INTEGER, "INTEGER" );
                registerHibernateType( Types.CHAR, "CHARACTER(1)" );
                registerHibernateType( Types.VARCHAR, "VARCHAR($l)" );
                registerHibernateType( Types.FLOAT, "FLOAT" );
                registerHibernateType( Types.DOUBLE, "DOUBLE PRECISION" );
                registerHibernateType( Types.DATE, "DATETIME" );
                registerHibernateType( Types.TIME, "DATETIME" );
                registerHibernateType( Types.TIMESTAMP, "DATETIME" );
                registerHibernateType( Types.VARBINARY, "VARBINARY($l)" );
                registerHibernateType( Types.NUMERIC, "DECIMAL(19,$l)" );
                
                

                //getDefaultProperties().setProperty(Environment.OUTER_JOIN, "false");
                getDefaultProperties().setProperty(Environment.STATEMENT_BATCH_SIZE, NO_BATCH);
        }

        public String getAddColumnString() {
                return "add";
        }
        public String getNullColumnString() {
                return " null";
        }
        public boolean qualifyIndexName() {
                return false;
        }       
        public boolean supportsForUpdate() {
                return false;
        }

    public boolean supportsIdentityColumns() {
        return true;
    }
    public String getIdentitySelectString() {
        return "select @@identity";
    }
    public String getIdentityColumnString() {
        return "IDENTITY NOT NULL";
    }
        public String getNoColumnsInsertString() {
                return "DEFAULT VALUES";
        }
}
