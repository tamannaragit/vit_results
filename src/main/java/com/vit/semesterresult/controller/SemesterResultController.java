package com.vit.semesterresult.controller;

import com.vit.semesterresult.form.SemesterResultForm;
import com.vit.semesterresult.form.SubjectMarksForm;
import com.vit.semesterresult.service.SemesterResultService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.vit.semesterresult.service.DuplicateSemesterResultException;

@Controller
public class SemesterResultController {
    private final SemesterResultService semesterResultService;
    public SemesterResultController(SemesterResultService semesterResultService) { this.semesterResultService = semesterResultService; }
    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("semesterResultForm", blankForm());
        return "index";
    }
    @PostMapping("/results")
    public String prepareResult(@Valid @ModelAttribute SemesterResultForm semesterResultForm, BindingResult bindingResult) {
        semesterResultService.normalize(semesterResultForm);
        semesterResultService.duplicateSubjectIndexes(semesterResultForm).forEach(index ->
                bindingResult.rejectValue("subjects[" + index + "].subjectName", "duplicate.subjectName",
                        "This subject is already entered. Use four different subject names."));
        if (bindingResult.hasErrors()) return "index";
        try {
            Long resultId = semesterResultService.prepareResult(semesterResultForm).getId();
            return "redirect:/results/" + resultId;
        } catch (DuplicateSemesterResultException exception) {
            bindingResult.reject("duplicate.result", exception.getMessage());
            return "index";
        }
    }
    @GetMapping("/results/{id}")
    public String showResult(@PathVariable Long id, Model model) {
        model.addAttribute("result", semesterResultService.getResult(id));
        return "result";
    }
    private SemesterResultForm blankForm() {
        SemesterResultForm form = new SemesterResultForm();
        for (int i = 0; i < 4; i++) form.getSubjects().add(new SubjectMarksForm());
        return form;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String resultNotFound(Model model) {
        model.addAttribute("errorMessage", "The requested result was not found or the request was invalid.");
        return "error";
    }
}
