package aitu.network.aitunetwork.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChatUtilsTest {

    @Test
    void generateOneToOneChatId_shouldThrowUOE_whenThreeIdsPassed() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        List<String> ids = List.of(id1, id2, id3);
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> ChatUtils.generateOneToOneChatId(ids),
                "Unsupported operation exception should be thrown when ids size is not 2");
        assertThat(exception.getMessage(), equalTo("Only two ids is valid for one to one chat id"));
    }

    @Test
    void generateOneToOneChatId_shouldReturnSameChatId_whenIdsPassedWithTwoPossibleWays() {
        String id1 = "id1";
        String id2 = "id2";
        String firstWay = ChatUtils.generateOneToOneChatId(List.of(id1, id2));
        String secondWay = ChatUtils.generateOneToOneChatId(List.of(id2, id1));

        assertThat("Two possible ids should generate same chat id", firstWay.equals(secondWay));
    }
}
