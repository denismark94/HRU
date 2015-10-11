import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Применить дополнительные меры безопасности? (y|n):");
        Model.security = scanner.nextLine().contains("y");
        System.out.println("Введите номер сценария угрозы:\n" +
                "1: admin имеет полные права на папку o2\n" +
                "2: admin может только читать o2, user может давать права на o2\n" +
                "3: admin может только читать o2, user не может давать права на o2");
        int threat = scanner.nextInt();
        Model.InitializeThreatScenario(threat);


    }
}
