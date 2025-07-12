package net.uebliche.demo;

import net.uebliche.server.GameServer;

public class Main {

    public static void main(String[] args) {
        GameServer.init();
        new Main().testSettings();
    }

    private void testSettings() {
//        var classicFFA = new ClassicFFASettings();
//        classicFFA.testClassicString = "ja";
//        settingsRepository.insert(classicFFA);
        var id = new ObjectId("68710dc35ebf0d9ddaaec68b");
        //  var found = settingsRepository.find(id, ClassicFFASettings.class);
        log.info("Found: {}", found);
        log.info("Found class: {}", found.getClass());
    }

}
