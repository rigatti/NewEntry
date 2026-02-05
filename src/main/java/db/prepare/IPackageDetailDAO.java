package db.prepare;

import java.util.List;
import java.util.StringTokenizer;

public interface IPackageDetailDAO {

	public List<PackageDetail> get(String productCode, StringTokenizer orderNumbers);

}