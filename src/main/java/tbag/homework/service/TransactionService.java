package tbag.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tbag.homework.model.Account;
import tbag.homework.model.User;
import tbag.homework.model.dto.CashDTO;
import tbag.homework.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final UserRepository userRepository;

    public double deposit(CashDTO cashDTO) {
        User user = userRepository.findById(cashDTO.getUserId()).orElseThrow();
//        Account account =
    }

}
