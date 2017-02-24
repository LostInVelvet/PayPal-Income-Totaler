import java.io.*;
import java.util.*;
import java.io.BufferedWriter;

public class paypalIncomeTotaler {
    public static void main(String[] args) throws IOException{ 
        List<String> companyName = new ArrayList<>();
        List<Float> money = new ArrayList<>();
        List<String> moneyType = new ArrayList<>();
        
        GetIncome(companyName, money, moneyType);
        SortIncome(companyName, money, moneyType, 0, money.size()-1);
        PrintIncome(companyName, money, moneyType);
    }

    
    
    
// ***********************************
// **          Get Income           **
// ***********************************
    
    public static void GetIncome(List<String> companyName, List<Float> money, List<String> moneyType) throws FileNotFoundException, IOException{
        String line;
        String lastCompany = "";
        String lastMoney;
        boolean haveName = false;
        int monEnd;
        int index = 0;
        FileInputStream input = null;
        Scanner file = null;
        
        
        try {
            // src/ is necessary due to Netbean's file system. It's unnecessary for other compilers.
            input = new FileInputStream("src/paypal-income.txt");
            file = new Scanner(input, "UTF-8");
            
            while (file.hasNextLine()) {
                line = file.nextLine();
                
                if(line.charAt(0) != '+'){
                    if(haveName == false && (line.substring(line.lastIndexOf(" ")+1)).equals("Received")){
                        lastCompany = line.substring(0, (line.lastIndexOf(" ")-7));
                        index = GetCompanyIndex(companyName, lastCompany);
                        haveName = true;
                    }
                }
                else {
                    haveName = false;
                    monEnd = 3 + line.lastIndexOf(".");
                    lastMoney = line.substring(3, monEnd);
                    
                    if(index == companyName.size()){
                        AddItem(companyName, money, moneyType, line, lastCompany, lastMoney, index, monEnd);   
                    }
                    else {
                        System.out.println("*** " + money.get(index) + "  +  " + Double.valueOf(lastMoney) + "  =  " + (money.get(index) + Float.valueOf(lastMoney)));
                    //    System.out.print((money.get(index) + Float.valueOf(lastMoney)) + "   ");
                        
                        money.set(index, money.get(index) + Float.valueOf(lastMoney));
                        
//                        System.out.println(money.get(index));
                    }
                }
            }
            if (file.ioException() != null) {
                throw file.ioException();
            }
        }  finally {
            if(input != null)
                input.close();
            if(file != null)
                file.close();
        }
     }
    
    
        public static void AddItem(List<String> companyName, List<Float> money, List<String> moneyType, String line, String lastCompany, String lastMoney, int index, int monEnd){
        companyName.add(lastCompany);
        money.add(Float.valueOf(lastMoney));
        
        if(line.charAt(2) == '$'){
            moneyType.add(line.substring(2, 3));
        }
        else {
            moneyType.add(line.substring(monEnd, monEnd+3));
        }
    }
        
    
    public static int GetCompanyIndex(List<String> companyName, String checkCompany){
        boolean found = false;
        int index = companyName.size();
        
        for(int i = 0; i < companyName.size() && !found; i++){
            if((companyName.get(i)).equals(checkCompany)){
                found = true;
                index = i;
            }
        }
        
        return index;
    }
    
    
    
    
// ***********************************
// **          Quicksort            **
// ***********************************
    
    public static void SortIncome(List<String> company, List<Float> money, List<String> moneyType, int lowerIndex, int upperIndex){
        if(lowerIndex < upperIndex){
            int mid = Partition(company, money, moneyType, lowerIndex, upperIndex);
            SortIncome(company, money, moneyType, lowerIndex, mid-1);
            SortIncome(company, money, moneyType, mid+1, upperIndex);
        }
    }
    
    
    public static int Partition(List<String> company, List<Float> money, List<String> moneyType, int lowerIndex, int upperIndex){
        int pivot = lowerIndex;
        
        for(int i = lowerIndex; i < upperIndex; i++){
            if(money.get(i) >= money.get(upperIndex)){
                Swap(company, money, moneyType, i, pivot);
                pivot++;
            }
        }
        Swap(company, money, moneyType, upperIndex, pivot);
        
        return pivot;
    }
    
    
    public static void Swap(List<String> company, List<Float> money, List<String> moneyType, int spot1, int spot2){
        String tempCompany = company.get(spot1);
        company.set(spot1, company.get(spot2));
        company.set(spot2, tempCompany);
        
        Float tempMoney = money.get(spot1);
        money.set(spot1, money.get(spot2));
        money.set(spot2, tempMoney);
        
        String tempMoneyType = moneyType.get(spot1);
        moneyType.set(spot1, moneyType.get(spot2));
        moneyType.set(spot2, tempMoneyType);
    }


    
    
// ***********************************
// **            Print              **
// ***********************************
    
    public static void PrintIncome(List<String> companyName, List<Float> money, List<String> moneyType) throws IOException{
        int i;
        float totalUnder50 = 0;
        float total;

        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("src/Income-Report.text"), "UTF-8"))){
                i = PrintAmounts(companyName, money, moneyType, 0, false, writer);
                
                totalUnder50 = GetTotal(money, moneyType, i, totalUnder50);
                PrintTotalUnder50(totalUnder50, writer);
                
                total = GetTotal(money, moneyType, 0, totalUnder50);
                PrintTotal(total, writer);
                
                
                for(int k = 0; k < 5; k++){
                    writer.newLine();
                }
                
                writer.write("             Other Income Amounts");
                writer.newLine();
                
                int unnecessary = PrintAmounts(companyName, money, moneyType, i, true, writer);
        }
    }
    
    
    public static float GetTotal(List<Float> money, List<String> moneyType, int i, float totalUnder50){
        float total = 0;
        float GBPtoUSD = 1.25f;
        float EURtoUSD = 1.06f;
        
        for(int j = i; j < money.size(); j++){
            switch (moneyType.get(j)) {
                case "$":
                    total += money.get(j);
                    break;
                case "GBP":
                    total += money.get(j)*GBPtoUSD;
                    break;
                case "EUR":
                    total += money.get(j)*EURtoUSD;
                    break;
                default:
                    System.out.println("*** Can't total " + moneyType.get(i) + " - Fix me in function GetTotal ***");
                    break;
            }
        }
        
        return total;
    }
    
    
    public static int PrintAmounts(List<String> companyName, List<Float> money, List<String> moneyType, int minIndex, boolean printUnder50, BufferedWriter writer) throws IOException{  
        int i;
        
        for(i = minIndex; i < money.size() && (printUnder50 || money.get(i) >= 50); i++){
            writer.write(companyName.get(i));
            for(int j = (companyName.get(i)).length(); j < 40; j++){
                writer.write(" ");
            }

            if((moneyType.get(i)).equals("$")){
                writer.write(moneyType.get(i) + String.format("%.2f", money.get(i)));
            }
            else {
                writer.write(money.get(i) + " " + moneyType.get(i));
            }

            writer.newLine();
        }
            
        return i;
    }
   

    public static void PrintTotalUnder50(float totalUnder50, BufferedWriter writer) throws IOException{
        List<String> companyName = new ArrayList<>();
        List<Float> money = new ArrayList<>();
        List<String> moneyType = new ArrayList<>();

        companyName.add("Other Income");
        money.add(totalUnder50);
        moneyType.add("$");

        PrintAmounts(companyName, money, moneyType, 0, true, writer);
    }

    
    public static void PrintTotal(float total, BufferedWriter writer) throws IOException{
        List<String> companyName = new ArrayList<>();
        List<Float> money = new ArrayList<>();
        List<String> moneyType = new ArrayList<>();

        companyName.add("Total:");
        money.add(total);
        moneyType.add("$");
        
        for(int j = 0; j < 50; j++){
            writer.write("-");
        }
        
        writer.newLine();
        
        PrintAmounts(companyName, money, moneyType, 0, true, writer);
        
    }
 
}