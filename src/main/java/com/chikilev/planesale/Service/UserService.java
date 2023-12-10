package com.chikilev.planesale.Service;

import com.chikilev.planesale.Entity.OrderEntity;
import com.chikilev.planesale.Entity.ReservationEntity;
import com.chikilev.planesale.Entity.User;
import com.chikilev.planesale.Response.CartRequest;
import com.chikilev.planesale.Response.CartResponse;
import com.chikilev.planesale.Repository.OrderRepo;
import com.chikilev.planesale.Repository.ReservationRepo;
import com.chikilev.planesale.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo repo;
    
    private final OrderRepo orderRepo;

    private final ReservationRepo reservationRepo;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepo repo, PasswordEncoder encoder, ReservationRepo reservationRepo, OrderRepo orderRepo) {
        this.repo = repo;
        this.encoder = (BCryptPasswordEncoder) encoder;
        this.reservationRepo = reservationRepo;
        this.orderRepo = orderRepo;
    }

    public boolean addUser(String userName, String Password){
//        if(repo.findByUsername(userName) != null) {
//            return false;
//        }
        User user = new User();
        user.setUsername(userName);
        user.setPassword(encoder.encode(Password));
        repo.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String addToCart(CartRequest cartRequest) {
        ReservationEntity reservationEntity = new ReservationEntity();
        // todo :Check if flightcode is correct
        reservationEntity.setFlightCode(cartRequest.getFlightCode());
        reservationEntity.setName(cartRequest.getName());
        reservationEntity.setSurname(cartRequest.getSurname());
        reservationEntity.setEmail(cartRequest.getEmail());
        reservationEntity.setPhone(cartRequest.getPhone());
        User user = repo.findByUsername(cartRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        reservationEntity.setUser(user);
        reservationRepo.save(reservationEntity);
        return "success";
    }

    public List<CartResponse> getCart(String username) {
        List<CartResponse> responses = new ArrayList<CartResponse>();
        Iterable<ReservationEntity> reservationEntities = reservationRepo.findAllByUser_Username(username);
        reservationEntities.forEach(reservationEntity -> responses.add(new CartResponse(reservationEntity)));
        return responses;
    }

    @Transactional
    public String clearCart(String extractUsername) {
        reservationRepo.deleteAllByUser_Username(extractUsername);
        return "success";
    }

    public String removeFromCart(String extractUsername, String flightCode) {
        reservationRepo.deleteByUser_UsernameAndFlightCode(extractUsername, flightCode);
        return "success";
    }

    public String order(String extractUsername) {
        OrderEntity orderEntity = new OrderEntity();
        Iterable<ReservationEntity> reservationEntitiesIter = reservationRepo.findAllByUser_Username(extractUsername);
        List<ReservationEntity> reservationEntities = new ArrayList<ReservationEntity>();
        reservationEntitiesIter.forEach(reservationEntities::add);
        for (ReservationEntity reservationEntity : reservationEntities) {
            orderEntity.setFlightCode(reservationEntity.getFlightCode());
            orderEntity.setName(reservationEntity.getName());
            orderEntity.setSurname(reservationEntity.getSurname());
            orderEntity.setEmail(reservationEntity.getEmail());
            orderEntity.setPhone(reservationEntity.getPhone());
            orderEntity.setUser(reservationEntity.getUser());
            orderRepo.save(orderEntity);
        }
        return "Success";
    }
}