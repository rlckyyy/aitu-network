package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.common.annotations.CurrentUser;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.entity.FriendRequest;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.enums.FriendRequestStatus;
import aitu.network.aitunetwork.service.FriendshipService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;

    @GetMapping("/received")
    List<FriendRequest> getReceivedFriendRequests(
            @RequestParam(required = false) FriendRequestStatus status, @CurrentUser CustomUserDetails user) {
        return friendshipService.getRequests(status, user.user());
    }

    @GetMapping("/{userId}")
    List<User> getUserFriends(@PathVariable String userId) {
        return friendshipService.getUserFriendList(userId);
    }

    @GetMapping("/sent")
    List<FriendRequest> getSentFriendRequests(
            @RequestParam(required = false) FriendRequestStatus status,
            @CurrentUser CustomUserDetails user
    ) {
        return friendshipService.getSentRequests(status, user.user());
    }

    @PostMapping("/request/{userId}")
    FriendRequest sendFriendRequest(
            @PathVariable @NotBlank(message = "errors.400.users.id") String userId,
            @CurrentUser CustomUserDetails user
    ) {
        return friendshipService.sendFriendRequest(userId, user.user());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/request/{requestId}")
    void deleteFriendRequest(@PathVariable @NotBlank(message = "errors.400.requests.id") String requestId, @CurrentUser CustomUserDetails user) {
        friendshipService.deleteRequest(requestId, user.user());
    }

    @PutMapping("/request/{requestId}/respond")
    FriendRequest respondRequest(@RequestParam FriendRequestStatus status,
                                 @PathVariable String requestId,
                                 @CurrentUser CustomUserDetails user) {
        return friendshipService.respondRequest(requestId, status, user.user());
    }
}
