package aitu.network.aitunetwork.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class ChatUtilsTest {

    @Test
    void generateOneToOneChatId_shouldReturnSameChatId_whenIdsPassedWithTwoPossibleWays() {
        String id1 = "id1";
        String id2 = "id2";
        String firstWay = ChatUtils.generateOneToOneChatId(List.of(id1, id2));
        String secondWay = ChatUtils.generateOneToOneChatId(List.of(id2, id1));

        assertThat("Two possible ids should generate same chat id", firstWay.equals(secondWay));
    }
}
