package com.backend.server.security;

import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.backend.server.companies.Company;
import com.backend.server.companies.CompanyAppEmailsService;
import com.backend.server.companies.CompanyApprovedEmails;
import com.backend.server.companies.CompanyService;
import com.backend.server.utility.LoginResponse;
import com.backend.server.utility.Role;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/group9/")
public class AdminController {
    private final SecurityService securityService;
    private final CompanyService companyService;
    private final CompanyAppEmailsService approvedemailsservice;

    @GetMapping("/admin")
    public String admin() {
        return "login";   // ensin login ennenkui pääsee admin sivulle
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        LoginResponse loginResponse = securityService.login(email, password);
        if (loginResponse != null) {
            // täytä model companytiedoilla ja company approved emails tiedoilla ja palauta amdin sivu
            List<Company> companies = companyService.getCompanies();
            List<CompanyApprovedEmails> approvedEmails = approvedemailsservice.getCompanyApprovedEmails();
            // modeliin lisätään attribuutit
            model.addAttribute("companies", companies);
            model.addAttribute("approvedEmails", approvedEmails);
            return "admin";
        } else {
            return "login";
        }
    }

    @PostMapping("/addcompany")
    public String addCompany(@RequestParam String companyName){
        Company company = new Company();
        company.setCompanyName(companyName);
        companyService.saveCompany(company);
        return "redirect:/api/group9/admin";
    }

    @PostMapping("/addemail")
    public String addEmail(@RequestParam String email, @RequestParam String companyId, @RequestParam String role){
        CompanyApprovedEmails approvedEmails = new CompanyApprovedEmails();
        approvedEmails.setEmail(email);
        approvedEmails.setCompany(companyService.getCompanyById(Long.parseLong(companyId)));
        approvedEmails.setRole(Role.valueOf(role));
        approvedemailsservice.saveCompanyApprovedEmails(approvedEmails);
        return "redirect:/api/group9/admin";
    }


    @PostMapping("/addboth")
public String addBoth(@RequestParam String companyName, @RequestParam String email, @RequestParam String role) {
    Company company = new Company();
    company.setCompanyName(companyName);
    companyService.saveCompany(company);

    CompanyApprovedEmails approvedEmails = new CompanyApprovedEmails();
    approvedEmails.setEmail(email);
    approvedEmails.setCompany(company);
    approvedEmails.setRole(Role.valueOf(role)); 
    approvedemailsservice.saveCompanyApprovedEmails(approvedEmails);

    return "redirect:/api/group9/admin";
}


    @GetMapping("/search")
    public String refreshLists(Model model) {
        List<Company> companies = companyService.getCompanies();
        List<CompanyApprovedEmails> approvedEmails = approvedemailsservice.getCompanyApprovedEmails();
        
        model.addAttribute("companies", companies);
        model.addAttribute("approvedEmails", approvedEmails);
        
        // Return a fragment of the page that contains the updated list, not the whole 'admin' view
        return "fragments/companyList :: company-list";
    }
    
    


}
