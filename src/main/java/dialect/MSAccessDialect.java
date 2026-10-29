package dialect;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;

public class MSAccessDialect extends Dialect {

	public MSAccessDialect() {
                super();
                getDefaultProperties().setProperty(Environment.STATEMENT_BATCH_SIZE, "0");
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
