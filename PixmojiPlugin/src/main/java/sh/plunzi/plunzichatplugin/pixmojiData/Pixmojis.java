package sh.plunzi.plunzichatplugin.pixmojiData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pixmojis {
    public List<Pixmoji> pixmojiList = new ArrayList<>();

    public static Pixmoji nullmoji = new Pixmoji("null", '\uE000');

    public Pixmojis() {
        pixmojiList.add(new Pixmoji("angryDevil", '\uE001'));
        pixmojiList.add(new Pixmoji("happy1", '\uE002'));
        pixmojiList.add(new Pixmoji("coldFace", '\uE003'));
        pixmojiList.add(new Pixmoji("kissHeart", '\uE004'));
        pixmojiList.add(new Pixmoji("toungeOut", '\uE005'));
        pixmojiList.add(new Pixmoji("vomit", '\uE006'));
        pixmojiList.add(new Pixmoji("pixmojiGivingHeadToShrek", '\uE006'));
        pixmojiList.add(new Pixmoji("hush1", '\uE007'));
        pixmojiList.add(new Pixmoji("hush2", '\uE008'));
        pixmojiList.add(new Pixmoji("faceInHands", '\uE009'));
        pixmojiList.add(new Pixmoji("raisedEyeBrow", '\uE010'));
        pixmojiList.add(new Pixmoji("cryLaugh", '\uE012'));
        pixmojiList.add(new Pixmoji("happyTounge", '\uE013'));
        pixmojiList.add(new Pixmoji("happy2", '\uE014'));
        pixmojiList.add(new Pixmoji("awkwardSmile", '\uE015'));
        pixmojiList.add(new Pixmoji("happySquint", '\uE016'));
        pixmojiList.add(new Pixmoji("hot", '\uE017'));
        pixmojiList.add(new Pixmoji("kissBlush", '\uE018'));
        pixmojiList.add(new Pixmoji("kiss", '\uE019'));
        pixmojiList.add(new Pixmoji("sob", '\uE020'));
        pixmojiList.add(new Pixmoji("melting", '\uE021'));
        pixmojiList.add(new Pixmoji("money", '\uE022'));
        pixmojiList.add(new Pixmoji("nausea", '\uE023'));
        pixmojiList.add(new Pixmoji("beg", '\uE024'));
        pixmojiList.add(new Pixmoji("rofl", '\uE025'));
        pixmojiList.add(new Pixmoji("headpat", '\uE026'));
        pixmojiList.add(new Pixmoji("shush", '\uE027'));
        pixmojiList.add(new Pixmoji("skullBones", '\uE028'));
        pixmojiList.add(new Pixmoji("skull", '\uE029'));
        pixmojiList.add(new Pixmoji("sleeping", '\uE030'));
        pixmojiList.add(new Pixmoji("smile", '\uE031'));
        pixmojiList.add(new Pixmoji("halo", '\uE032'));
        pixmojiList.add(new Pixmoji("heartEyes", '\uE033'));
        pixmojiList.add(new Pixmoji("smileHearts", '\uE034'));
        pixmojiList.add(new Pixmoji("evilDevil", '\uE035'));
        pixmojiList.add(new Pixmoji("squintEyesHands", '\uE036'));
        pixmojiList.add(new Pixmoji("blush", '\uE037'));
        pixmojiList.add(new Pixmoji("sunglasses", '\uE038'));
        pixmojiList.add(new Pixmoji("sweat", '\uE039'));
        pixmojiList.add(new Pixmoji("sneeze", '\uE040'));
        pixmojiList.add(new Pixmoji("squintTounge", '\uE041'));
        pixmojiList.add(new Pixmoji("starstruck", '\uE042'));
        pixmojiList.add(new Pixmoji("huh", '\uE043'));
        pixmojiList.add(new Pixmoji("thinking", '\uE043'));
        pixmojiList.add(new Pixmoji("upsideDown", '\uE044'));
        pixmojiList.add(new Pixmoji("wink", '\uE045'));
        pixmojiList.add(new Pixmoji("wink2", '\uE046'));
        pixmojiList.add(new Pixmoji("rollWink", '\uE047'));
        pixmojiList.add(new Pixmoji("zipped", '\uE048'));
        pixmojiList.add(new Pixmoji("uwu", '\uE049'));
        pixmojiList.add(new Pixmoji("neutral", '\uE050'));
        pixmojiList.add(new Pixmoji("flushed", '\uE051'));

    }

    public Pixmoji getPixmoji(String name) {

        for(Pixmoji pixmoji : pixmojiList) {
            if (name.toLowerCase().equals(pixmoji.getName().toLowerCase())) {
                return pixmoji;
            }
        }

        return nullmoji;
    }

    public Pixmoji getPixmoji(char unicodeChar) {

        for(Pixmoji pixmoji : pixmojiList) {
            if (unicodeChar == pixmoji.getUnicodeChar()) {
                return pixmoji;
            }
        }

        return nullmoji;
    }

    public Pixmoji getRandom() {
        Random random = new Random();
        int index = random.nextInt(pixmojiList.size());

        return pixmojiList.get(index);
    }

    public boolean isInList(String name) {
        for(Pixmoji pixmoji : pixmojiList) {
            if (name.equals(pixmoji.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isInList(char unicodeChar) {
        for(Pixmoji pixmoji : pixmojiList) {
            if (unicodeChar == pixmoji.getUnicodeChar()) {
                return true;
            }
        }
        return false;
    }

}

