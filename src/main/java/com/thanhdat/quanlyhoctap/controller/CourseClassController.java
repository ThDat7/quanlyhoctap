package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.CourseClassCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.CourseClassService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/course-classes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseClassController {
    CourseClassService courseClassService;

    @GetMapping("/semester/{semesterId}/current-teacher-teaching")
    public ApiResponse<List<TeacherCourseClassTeachingResponse>> getCurrentTeacherTeachingBySemesterCourseClass(@PathVariable Long semesterId){
        return ApiResponse.ok(courseClassService.getCurrentTeacherTeaching(semesterId));
    }

    @GetMapping("/semester/{semesterId}/course/{courseId}")
    public ApiResponse<DataWithCounterDto<CourseClassResponse>> getCourseClassBySemesterAndCourse(@PathVariable Long semesterId, @PathVariable Long courseId,
                                                                                                  @RequestParam Map<String, String> params) {
        return ApiResponse.ok(courseClassService.getCourseClassBySemesterAndCourse(semesterId, courseId, params));
    }

//    2 endpoint for course registration

    @GetMapping("/semester/{semesterId}/course-with-courseclass-count")
    public ApiResponse<DataWithCounterDto<CourseWithCourseClassCountResponse>>
    getCourseWithCourseClassCountBySemester(@PathVariable Long semesterId,
                                            @RequestParam Map<String, String> params) {
        return ApiResponse.ok(courseClassService.getCourseWithCourseClassCountBySemester(semesterId, params));
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseClassViewCrudResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(courseClassService.getById(id));
    }

    @GetMapping("/course/{courseId}/semester/{semesterId}/select-options-available-teacher")
    public ApiResponse<List<SelectOptionResponse>> getSelectOptionsAvailableTeacher(@PathVariable Long courseId,
                                                                                      @PathVariable Long semesterId) {
          return ApiResponse.ok(courseClassService.getSelectOptionsAvailableTeacher(courseId, semesterId));
    }

    @GetMapping("/course/{courseId}/semester/{semesterId}/select-options-available-student-class")
    public ApiResponse<List<SelectOptionResponse>> getSelectOptionsAvailableStudentClass(@PathVariable Long courseId,
                                                                                      @PathVariable Long semesterId) {
          return ApiResponse.ok(courseClassService.getSelectOptionsAvailableStudentClass(courseId, semesterId));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody @Valid CourseClassCrudRequest createRequest) {
        courseClassService.create(createRequest);
        return ApiResponse.ok();
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody @Valid CourseClassCrudRequest updateRequest) {
        courseClassService.update(id, updateRequest);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        courseClassService.delete(id);
        return ApiResponse.ok();
    }
}

