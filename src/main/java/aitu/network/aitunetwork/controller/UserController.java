package aitu.network.aitunetwork.controller;


import aitu.network.aitunetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public String hello() {
        return "hello";
    }

    // Example 1:
    //
    //Input: nums = [1,1,1,2,2,3], k = 2
    //Output: [1,2]
    //Example 2:
    //
    //Input: nums = [1], k = 1
    //Output: [1]
    public static void main(String[] args) {
        List<List<Integer>> list = threeSum(new int[]{-1, 0, 1, 2, -1, -4});
        System.out.println(list);
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        int i = 0;
        int j = i + 1; // -4 -1 -1 0 1 2
        int k = nums[nums.length - 1]; // right + left + t = 0 // right + left = -t
        List<List<Integer>> list = new ArrayList<>();
        while (i <= k) { //
            int sum = nums[i] + nums[j] + nums[k];
            if (sum == 0 && i == j && j == k & i == k) {
                list.add(List.of(i, j, k));
            } else if (sum > 0) {
                k--;
            } else if (sum < 0) {
                j++;
            }
        }
        return null;
    }

}
