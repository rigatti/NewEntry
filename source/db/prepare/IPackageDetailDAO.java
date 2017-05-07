package db.prepare;

import java.util.ArrayList;
import java.util.StringTokenizer;

public interface IPackageDetailDAO {

	public  ArrayList<PackageDetail> get(String productCode, StringTokenizer orderNumbers);

}