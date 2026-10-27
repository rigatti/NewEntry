package db.prepare;

import java.util.Collection;
import java.util.List;

public interface IPackageDetailDAO {

	public List<PackageDetail> get(String productCode, Collection<Integer> orderNumbers);

}