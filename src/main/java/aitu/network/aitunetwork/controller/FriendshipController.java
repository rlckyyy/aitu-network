package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.model.entity.FriendRequest;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.enums.FriendRequestStatus;
import aitu.network.aitunetwork.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;

    @GetMapping("/received")
    List<FriendRequest> getReceivedFriendRequests(
            @RequestParam(required = false) FriendRequestStatus status) {
        return friendshipService.getRequests(status);
    }
    @GetMapping("/{userId}")
    List<User> getUserFriends(@PathVariable String userId){
        return friendshipService.getUserFriendList(userId);
    }

    @GetMapping("/sent")
    List<FriendRequest> getSentFriendRequests(
            @RequestParam(required = false) FriendRequestStatus status) {
        return friendshipService.getSentRequests(status);
    }

    @PostMapping("/request/{userId}")
    FriendRequest sendFriendRequest(@PathVariable String userId) {
        return friendshipService.sendFriendRequest(userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/request/{requestId}")
    void deleteFriendRequest(@PathVariable String requestId) {
        friendshipService.deleteRequest(requestId);
    }

    @PutMapping("/request/{requestId}/respond")
    FriendRequest respondRequest(@RequestParam FriendRequestStatus status,
                                 @PathVariable String requestId) {
        return friendshipService.respondRequest(requestId, status);
    }

}
