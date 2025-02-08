package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.model.entity.FriendRequest;
import aitu.network.aitunetwork.model.enums.FriendRequestStatus;
import aitu.network.aitunetwork.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @GetMapping("/own")
    List<FriendRequest> getFriendRequests(@RequestParam(required = false) FriendRequestStatus status) {
        return friendRequestService.getRequests(status);
    }

    @GetMapping("/mine")
    List<FriendRequest> getSentFriendRequests(@RequestParam(required = false) FriendRequestStatus status) {
        return friendRequestService.getSentRequests(status);
    }

    @PostMapping("/{userId}")
    FriendRequest sendFriendRequest(@PathVariable String userId) {
        return friendRequestService.sendFriendRequest(userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{requestId}")
    void deleteFriendRequest(@PathVariable String requestId) {
        friendRequestService.deleteRequest(requestId);
    }

    @PutMapping("/{requestId}")
    FriendRequest respondRequest(@RequestParam FriendRequestStatus status, @PathVariable String requestId){
        return friendRequestService.respondRequest(requestId, status);
    }

}
