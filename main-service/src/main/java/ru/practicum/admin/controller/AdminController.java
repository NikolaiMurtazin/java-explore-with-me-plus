package ru.practicum.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.sevice.CategoryService;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;

    //    ---- USER
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                                     @RequestParam(value = "from", defaultValue = "0") int from,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        if (ids != null && !ids.isEmpty()) {
            return userService.getUsersByIds(ids);
        } else {
            return userService.getAllUsers(from, size);
        }
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest dto) {
        return userService.create(dto);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        userService.delete(userId);
    }

    //    ----CATEGORY
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto dto) {
        return categoryService.createCategory(dto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        categoryService.delete(catId);
    }

    @PatchMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable long catId, @Valid @RequestBody CategoryDto dto) {
        dto.setId(catId);
        return categoryService.updateCategory(dto);
    }
}
