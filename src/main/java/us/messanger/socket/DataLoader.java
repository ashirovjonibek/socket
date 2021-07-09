package us.messanger.socket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("" +
                "    ______     ______    __    __    __   __        _______    __    __\n" +
                "   / ___  |   /   _  \\  |  \\  |  |  |  | |  |      |   ____|  |  |  / /\n" +
                "  /_/   | |  |   | |  | |   \\ |  |  |  | |  |___   |  |____   |  | / /\n" +
                "        | |  |   | |  | | |\\ \\|  |  |  | |   __ \\  |   ____|  |  |/ /\n" +
                "    ____/ |  |   |_|  | | | \\ |  |  |  | |  |__| | |  |____   |  |\\ \\\n" +
                "    \\____/    \\______/  |_|  \\|__|  |__| |__|___/  |_______|  |__| \\_\\ ");
    }
}
