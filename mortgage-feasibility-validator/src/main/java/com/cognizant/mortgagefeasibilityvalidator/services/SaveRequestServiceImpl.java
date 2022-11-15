
package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.model.Request;
import com.cognizant.mortgagefeasibilityvalidator.repositories.RequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SaveRequestServiceImpl implements SaveRequestService{

    @Autowired
    private final RequestRepository requestRepository;

    public SaveRequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Request saveRequest(Request request) {
        log.info("Saved mortgage request with ID " + request.getId() + " with " + request.getMortgages().size() + "mortgages.");
        return requestRepository.save(request);
    }
}
