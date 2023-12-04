package Memory;

public class ReservationStations {
	String[] tagmul;
	int[] busymul;
	String[] opmul;
	String[] vjmul;
	String[] vkmul;
	String[] qjmul;
	String[] qkmul;
	int[] addressmul;
	int maxmul;
	int[] linemul;
	
	String[] tagadd;
	int[] busyadd;
	String[] opadd;
	String[] vjadd;
	String[] vkadd;
	String[] qjadd;
	String[] qkadd;
	int[] addressadd;
	int maxadd;
	int[] lineadd;
	
	String[] tagload;
	int[] busyload;
	int[] addressload;
	int maxload;
	int[] lineload;
	
	String[] tagstore;
	int[] busystore;
	int[] addressstore;
	String[] vstore;
	String[] qstore;
	int maxstore;
	int[] linestore;
	
	public ReservationStations(int mul, int add, int load, int store, int maxmul, int maxadd, int maxload, int maxstore) {
		Mul(mul, maxmul);
		Add(add, maxadd);
		Load(load, maxload);
		Store(store, maxstore);
	}
	
	public ReservationStations() {
		Mul(2, 8);
		Add(3, 3);
		Load(3, 2);
		Store(3, 2);
	}
	
	public void Mul(int n, int count) {
		setTagmul(n);
		setBusymul(n);
		setOpmul(n);
		setVjmul(n);
		setVkmul(n);
		setQjmul(n);
		setQkmul(n);
		setAddressmul(n);
		setMaxmul(count);
		setLinemul(n);
	}
	
	public void Add(int n, int count) {
		setTagadd(n);
		setBusyadd(n);
		setOpadd(n);
		setVjadd(n);
		setVkadd(n);
		setQjadd(n);
		setQkadd(n);
		setAddressadd(n);
		setMaxadd(count);
		setLineadd(n);
	}
	
	public void Load(int n, int count) {
		setTagload(n);
		setBusyload(n);
		setAddressload(n);
		setMaxload(count);
		setLineload(n);
	}
	
	public void Store(int n, int count) {
		setTagstore(n);
		setBusystore(n);
		setAddressstore(n);
		setVstore(n);
		setQstore(n);
		setMaxstore(count);
		setLinestore(n);
	}
	
	public String[] getTagmul() {
		return tagmul;
	}
	private void setTagmul(int n) {
		tagmul = new String[n];
		for(int i = 1; i <= n; i++) {
			tagmul[i-1] = "M"+i;
		}
	}
	public int[] getBusymul() {
		return busymul;
	}
	private void setBusymul(int n) {
		busymul = new int[n];
		for(int i=0;i<n;i++) {
			busymul[i]=0;
		}
	}
	public String[] getOpmul() {
		return opmul;
	}
	private void setOpmul(int n) {
		opmul = new String[n];
		for(int i = 0; i < n; i++) {
			opmul[i] = "";
		}
	}
	public String getVjmul(int line) {
		return vjmul[getTagUsingLinemul(line)];
	}
	private void setVjmul(int n) {
		vjmul = new String[n];
		for(int i = 0; i < n; i++) {
			vjmul[i] = "";
		}
	}
	public String getVkmul(int line) {
		return vkmul[getTagUsingLinemul(line)];
	}
	private void setVkmul(int n) {
		vkmul = new String[n];
		for(int i = 0; i < n; i++) {
			vkmul[i] = "";
		}
	}
	public String getQjmul(int line) {
		return qjmul[getTagUsingLinemul(line)];
	}
	private void setQjmul(int n) {
		qjmul = new String[n];
		for(int i = 0; i < n; i++) {
			qjmul[i] = "";
		}
	}
	public String getQkmul(int line) {
		return qkmul[getTagUsingLinemul(line)];
	}
	private void setQkmul(int n) {
		qkmul = new String[n];
		for(int i = 0; i < n; i++) {
			qkmul[i] = "";
		}
	}
	public int[] getAddressmul() {
		return addressmul;
	}
	private void setAddressmul(int n) {
		addressmul = new int[n];
		for(int i = 0; i < n; i++) {
			addressmul[i] = -1;
		}
	}
	public int[] getLinemul() {
		return linemul;
	}
	private void setLinemul(int n) {
		linemul = new int[n];
		for(int i = 0; i < n; i++) {
			linemul[i] = -1;
		}
	}
	private int getTagUsingLinemul(int n) {
		for(int i=0;i<tagmul.length;i++) {
			if(linemul[i]==n)
				return i;
		}
		return -1;
	}
	public boolean isOccupiedMul() {
		int n = -1;
		for(int i = 0;i < tagmul.length; i++) {
			if(busymul[i]==0) {
				n=i;
				break;
			}
		}
		if(n==-1)
			return true;
		else
			return false;
	}
	public void setOccupiedMul(String op, String vj, String vk, String qj, String qk, int address, int line) {
		int n = -1;
		for(int i = 0;i < tagmul.length; i++) {
			if(busymul[i]==0) {
				n=i;
				break;
			}
		}
		busymul[n]=1;
		opmul[n] = op;
		vjmul[n] = vj;
		vkmul[n] = vk;
		qjmul[n] = qj;
		qkmul[n] = qk;
		addressmul[n] = address;
		linemul[n] = line;
	}
	
	public String[] getTagadd() {
		return tagadd;
	}
	private void setTagadd(int n) {
		tagadd = new String[n];
		for(int i = 1; i <= n; i++) {
			tagadd[i-1] = "A"+i;
		}
	}
	public int[] getBusyadd() {
		return busyadd;
	}
	private void setBusyadd(int n) {
		busyadd = new int[n];
		for(int i=0;i<n;i++) {
			busyadd[i]=0;
		}
	}
	public String[] getOpadd() {
		return opadd;
	}
	private void setOpadd(int n) {
		opadd = new String[n];
		for(int i = 0; i < n; i++) {
			opadd[i] = "";
		}
	}
	public String getVjadd(int line) {
		return vjadd[getTagUsingLineadd(line)];
	}
	private void setVjadd(int n) {
		vjadd = new String[n];
		for(int i = 0; i < n; i++) {
			vjadd[i] = "";
		}
	}
	public String getVkadd(int line) {
		return vkadd[getTagUsingLineadd(line)];
	}
	private void setVkadd(int n) {
		vkadd = new String[n];
		for(int i = 0; i < n; i++) {
			vkadd[i] = "";
		}
	}
	public String getQjadd(int line) {
		return qjadd[getTagUsingLineadd(line)];
	}
	private void setQjadd(int n) {
		qjadd = new String[n];
		for(int i = 0; i < n; i++) {
			qjadd[i] = "";
		}
	}
	public String getQkadd(int line) {
		return qkadd[getTagUsingLineadd(line)];
	}
	private void setQkadd(int n) {
		qkadd = new String[n];
		for(int i = 0; i < n; i++) {
			qkadd[i] = "";
		}
	}
	public int[] getAddressadd() {
		return addressadd;
	}
	private void setAddressadd(int n) {
		addressadd = new int[n];
		for(int i = 0; i < n; i++) {
			addressadd[i] = -1;
		}
	}
	public int[] getLineadd() {
		return lineadd;
	}
	private void setLineadd(int n) {
		lineadd = new int[n];
		for(int i = 0; i < n; i++) {
			lineadd[i] = -1;
		}
	}
	private int getTagUsingLineadd(int n) {
		for(int i=0;i<tagadd.length;i++) {
			if(lineadd[i]==n)
				return i;
		}
		return -1;
	}
	public boolean isOccupiedAdd() {
		int n = -1;
		for(int i = 0;i < tagadd.length; i++) {
			if(busyadd[i]==0) {
				n=i;
				break;
			}
		}
		if(n==-1)
			return true;
		else
			return false;
	}
	public void setOccupiedAdd(String op, String vj, String vk, String qj, String qk, int address, int line) {
		int n = -1;
		for(int i = 0;i < tagadd.length; i++) {
			if(busyadd[i]==0) {
				n=i;
				break;
			}
		}
		busyadd[n]=1;
		opadd[n] = op;
		vjadd[n] = vj;
		vkadd[n] = vk;
		qjadd[n] = qj;
		qkadd[n] = qk;
		addressadd[n] = address;
		lineadd[n] = line;
	}
	
	public String[] getTagload() {
		return tagload;
	}
	private void setTagload(int n) {
		tagload = new String[n];
		for(int i = 1; i <= n; i++) {
			tagload[i-1] = "L"+i;
		}
	}
	public int[] getBusyload() {
		return busyload;
	}
	private void setBusyload(int n) {
		busyload = new int[n];
		for(int i=0;i<n;i++) {
			busyload[i]=0;
		}
	}
	public int getAddressload(int line) {
		return addressload[getTagUsingLineload(line)];
	}
	private void setAddressload(int n) {
		addressload = new int[n];
		for(int i = 0; i < n; i++) {
			addressload[i] = -1;
		}
	}
	public int[] getLineload() {
		return lineload;
	}
	private void setLineload(int n) {
		lineload = new int[n];
		for(int i = 0; i < n; i++) {
			lineload[i] = -1;
		}
	}
	private int getTagUsingLineload(int n) {
		for(int i=0;i<tagload.length;i++) {
			if(lineload[i]==n)
				return i;
		}
		return -1;
	}
	public boolean isOccupiedLoad() {
		int n = -1;
		for(int i = 0;i < tagload.length; i++) {
			if(busyload[i]==0) {
				n=i;
				break;
			}
		}
		if(n==-1)
			return true;
		else
			return false;
	}
	public void setOccupiedLoad(int address, int line) {
		int n = -1;
		for(int i = 0;i < tagload.length; i++) {
			if(busyload[i]==0) {
				n=i;
				break;
			}
		}
		busyload[n]=1;
		addressload[n] = address;
		lineload[n] = line;
	}
	
	public String[] getTagstore() {
		return tagstore;
	}
	private void setTagstore(int n) {
		tagstore = new String[n];
		for(int i = 1; i <= n; i++) {
			tagstore[i-1] = "S"+i;
		}
	}
	public int[] getBusystore() {
		return busyload;
	}
	private void setBusystore(int n) {
		busystore = new int[n];
		for(int i=0;i<n;i++) {
			busystore[i]=0;
		}
	}
	public int getAddressstore(int line) {
		return addressstore[getTagUsingLinestore(line)];
	}
	private void setAddressstore(int n) {
		addressstore = new int[n];
		for(int i = 0; i < n; i++) {
			addressstore[i] = -1;
		}
	}
	public String getVstore(int line) {
		return vstore[getTagUsingLinestore(line)];
	}
	private void setVstore(int n) {
		vstore = new String[n];
		for(int i = 0; i < n; i++) {
			vstore[i] = "";
		}
	}
	public String getQstore(int line) {
		return qstore[getTagUsingLinestore(line)];
	}
	private void setQstore(int n) {
		qstore = new String[n];
		for(int i = 0; i < n; i++) {
			qstore[i] = "";
		}
	}
	public int[] getLinestore() {
		return linestore;
	}
	private void setLinestore(int n) {
		linestore = new int[n];
		for(int i = 0; i < n; i++) {
			linestore[i] = -1;
		}
	}
	private int getTagUsingLinestore(int n) {
		for(int i=0;i<tagstore.length;i++) {
			if(linestore[i]==n)
				return i;
		}
		return -1;
	}
	public boolean isOccupiedStore() {
		int n = -1;
		for(int i = 0;i < tagstore.length; i++) {
			if(busystore[i]==0) {
				n=i;
				break;
			}
		}
		if(n==-1)
			return true;
		else
			return false;
	}
	public void setOccupiedStore(int address, String v, String q, int line) {
		int n = -1;
		for(int i = 0;i < tagstore.length; i++) {
			if(busystore[i]==0) {
				n=i;
				break;
			}
		}
		busystore[n]=1;
		addressstore[n] = address;
		vstore[n] = v;
		qstore[n] = q;
		linestore[n] = line;
	}
	
	public int getMaxmul() {
		return maxmul;
	}
	public int getMaxadd() {
		return maxadd;
	}
	public int getMaxload() {
		return maxload;
	}
	public int getMaxstore() {
		return maxstore;
	}
	private void setMaxmul(int max) {
		maxmul = max;
	}
	private void setMaxadd(int max) {
		maxadd = max;
	}
	private void setMaxload(int max) {
		maxload = max;
	}
	private void setMaxstore(int max) {
		maxstore = max;
	}
	public String searchRegister(String register) {
		for(int i=0;i<vjmul.length;i++) {
			if(vjmul[i].equals(register) || vkmul[i].equals(register)) 
				return tagmul[i];
		}
		for(int i=0;i<vjadd.length;i++) {
			if(vjadd[i].equals(register) || vkadd[i].equals(register)) 
				return tagadd[i];
		}
		for(int i=0;i<vstore.length;i++) {
			if(vstore[i].equals(register)) 
				return tagstore[i];
		}
		return "0";
	}
	
	public void writeWaiting(String tagdestination, int n) {
		for(int i=0;i<qjmul.length;i++) {
			if(qjmul[i].equals(tagdestination)) {
				vjmul[i] = "F"+n;
				qjmul[i] = "0";
			}
			if(qkmul[i].equals(tagdestination)) {
				vkmul[i] = "F"+n;
				qkmul[i] = "0";
			}
		}
		for(int i=0;i<qjadd.length;i++) {
			if(qjadd[i].equals(tagdestination)) {
				vjadd[i] = "F"+n;
				qjadd[i] = "0";
			}
			if(qkadd[i].equals(tagdestination)) {
				vkadd[i] = "F"+n;
				qkadd[i] = "0";
			}
		}
		for(int i=0;i<qstore.length;i++) {
			if(qstore[i].equals(tagdestination)) {
				vstore[i] = "F"+n;
				qstore[i] = "0";
			}
		}
	}
	public String getTagUsingLine(int n) {
		for(int i=0;i<tagmul.length;i++) {
			if(linemul[i]==n)
				return tagmul[i];
		}
		for(int i=0;i<tagadd.length;i++) {
			if(lineadd[i]==n)
				return tagadd[i];
		}
		for(int i=0;i<tagload.length;i++) {
			if(lineload[i]==n)
				return tagload[i];
		}
		for(int i=0;i<tagstore.length;i++) {
			if(linestore[i]==n)
				return tagstore[i];
		}
		return "";
	}
	public void setAvailable(int line) {
		String tag = getTagUsingLine(line);
		if(tag.startsWith("M")) {
			int n = getTagUsingLinemul(line);
			busymul[n] = 0;
			opmul[n] = "";
			vjmul[n] = "";
			vkmul[n] = "";
			qjmul[n] = "";
			qkmul[n] = "";
			addressmul[n] = -1;
			linemul[n] = -1;
		} else if(tag.startsWith("A")) {
			int n = getTagUsingLineadd(line);
			busyadd[n] = 0;
			opadd[n] = "";
			vjadd[n] = "";
			vkadd[n] = "";
			qjadd[n] = "";
			qkadd[n] = "";
			addressadd[n] = -1;
			lineadd[n] = -1;
		} else if(tag.startsWith("L")) {
			int n = getTagUsingLineload(line);
			busyload[n] = 0;
			addressload[n] = -1;
			lineload[n] = -1;
		} else if(tag.startsWith("S")) {
			int n = getTagUsingLinestore(line);
			busystore[n] = 0;
			addressstore[n] = -1;
			vstore[n] = "";
			qstore[n] = "";
			linestore[n] = -1;
		}	
	}
	
	public String toString() {
		System.out.println("--------------------------Mul--------------------------");
		System.out.println("Tag | Op |  Busy | Vj | Vq | Qj | Qk | A");
		for(int i=0;i<tagmul.length;i++) {
			System.out.print(tagmul[i] + "  | " + opmul[i] + " |  " + busymul[i] + "   | " + vjmul[i] + " | " + vkmul[i] + " | " + qjmul[i] + " | " + qkmul[i] + " | ");
			if(addressmul[i] != -1) {
				System.out.println(addressmul[i]);
			} else {
				System.out.println();
			}
		}
		System.out.println("--------------------------Add--------------------------");
		System.out.println("Tag | Op | Busy | Vj | Vq | Qj | Qk | A");
		for(int i=0;i<tagadd.length;i++) {
			System.out.print(tagadd[i] + "  | " + opadd[i] + " |  " + busyadd[i] + "   | " + vjadd[i] + " | " + vkadd[i] + " | " + qjadd[i] + " | " + qkadd[i] + " | ");
			if(addressadd[i] != -1) {
				System.out.println(addressadd[i]);
			} else {
				System.out.println();
			}
		}
		System.out.println("-------------------------Load-------------------------");
		System.out.println("Tag | Busy | Address");
		for(int i=0;i<tagload.length;i++) {
			System.out.print(tagload[i] + "  |  " + busyload[i] + "   | ");
			if(addressload[i] != -1) {
				System.out.println(addressload[i]);
			} else {
				System.out.println();
			}
		}
		System.out.println("-------------------------Store-------------------------");
		System.out.println("Tag | Busy | V | Q | Address");
		for(int i=0;i<tagstore.length;i++) {
			System.out.print(tagstore[i] + "  |  " + busystore[i] + "   | " + vstore[i] + " | " + qstore[i] + " | ");
			if(addressstore[i] != -1) {
				System.out.println(addressstore[i]);
			} else {
				System.out.println();
			}
		}
		return "";
	}
	
}
