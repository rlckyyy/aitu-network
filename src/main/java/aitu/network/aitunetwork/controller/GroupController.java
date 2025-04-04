package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.common.annotations.CurrentUser;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.GroupCreateDTO;
import aitu.network.aitunetwork.model.entity.Group;
import aitu.network.aitunetwork.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping("/{id}")
    Group getById(@PathVariable String id) {
        return groupService.findById(id);
    }

    @GetMapping("/search")
    List<Group> searchGroups(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) String userId) {
        return groupService.searchGroups(name, ownerId, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Group createGroup(@RequestBody GroupCreateDTO dto, @CurrentUser CustomUserDetails details) {
        return groupService.createGroup(dto, details);
    }

    @PostMapping("/follow/{groupId}")
    Group followGroup(@PathVariable String groupId, @CurrentUser CustomUserDetails details){
        return groupService.followGroup(groupId, details);
    }
}
