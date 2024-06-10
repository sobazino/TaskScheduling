// 2024 | Mehran Nosrati

package SobaZino;

public class Constants {
	// MAIN
	public static final int NO_OF_DATA_CENTERS = 1;
	public static final int NO_OF_USER = 1;
    public static final int NO_OF_VMS = 4;
    public static final int NO_OF_TASKS = 500;

    public static final int NO_PESNUMBER = 1;
    // VM
    public static final long VM_SIZE = 10000;
    public static final int VM_RAM = 512;
    public static final int[] VM_MIPS = {500,1000,2000};
    public static final long VM_BW = 1000;
    public static final String VM_VMM = "Xen";

    // DATACENTER
    public static final int DC_HOST = 2;
    public static final int DC_CPU = 6;
    public static final int DC_MIPS = 100000;
    public static final int DC_RAM = 16384;
    public static final long DC_STORAGE = 1000000;
    public static final int DC_BW = 100000;
    public static final String DC_ARCH = "x86";
    public static final String DC_OS = "Linux";
    public static final String DC_VMM = "Xen";
    public static final double DC_TIMEZONE = 10.0;
    public static final double DC_COST = 0.3;
    public static final double DC_COSTPERMEM = 0.05;
    public static final double DC_COSTPERSTORAGE = 0.01;
    public static final double DC_COSTPERBW = 0.01;
}
