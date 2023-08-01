package com.gdsc.wherewego.repository;

import com.gdsc.wherewego.domain.*;
import com.gdsc.wherewego.domain.category.District;
import com.gdsc.wherewego.domain.category.FoodType;
import com.gdsc.wherewego.domain.category.Theme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;


import static com.gdsc.wherewego.domain.enumCategory.DistrictEnum.BUK_GU;
import static com.gdsc.wherewego.domain.enumCategory.ThemeEnum.WALK;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("하루 일정 JPA 연결 테스트")
@Disabled
class DailyScheduleRepositoryTest extends RepositoryTest{
    private User user;
    private Schedule schedule;
    private DailyPlace dailyPlace;
    private DailySchedule dailySchedule;
    private Place place;
    private District district;
    private FoodType foodType;
    private Theme theme;
    @BeforeEach
    void init() {
        user = User.builder()
                .nickname("창윤")
                .email("aaa@gmail.com")
                .profileUrl("www.asd.com")
                .build();

        schedule = Schedule.builder()
                .user(user)
                .name("나의 일정")
                .startDate("2023/07/16")
                .endDate("2023/07/16")
                .build();

        foodType = FoodType.builder()
                .schedule(schedule)
                .type("한식")
                .build();

        theme = Theme.builder()
                .schedule(schedule)
                .type("사격")
                .build();

        district = District.builder()
                .schedule(schedule)
                .city("북구")
                .build();

        dailySchedule = DailySchedule.builder()
                .schedule(schedule)
                .date(LocalDate.of(2023,7,16))
                .build();

        place = Place.builder()
                .name("경북대학교")
                .district(BUK_GU)
                .theme(WALK)
                .latitude(35.8905477)
                .longitude(128.6121117)
                .build();

        dailyPlace = DailyPlace.builder()
                .place(place)
                .dailySchedule(dailySchedule)
                .build();
    }

    @Test
    @DisplayName("하루 일정을 저장한다.")
    void save() {
        //given, when
        User savedUser = users.save(user);
        Place savedPlace = places.save(place);
        Schedule savedSchedule = schedules.save(schedule);
        DailySchedule savedDaily = dailySchedules.save(dailySchedule);
        DailyPlace savedDailyPlace = dailyPlaces.save(dailyPlace);

        //then
        assertThat(savedDaily.getId()).isNotNull();
        assertThat(savedDaily).isEqualTo(dailySchedule);
    }

    @Test
    @DisplayName("일정에 포함된 하루 일정을 찾아올 수 있다.")
    void findBySchedule() {
        //given
        User savedUser = users.save(user);
        Place savedPlace = places.save(place);
        Schedule savedSchedule = schedules.save(schedule);
        DailySchedule savedDaily = dailySchedules.save(dailySchedule);
        DailyPlace savedDailyPlace = dailyPlaces.save(dailyPlace);

        //when
        List<DailySchedule> findDaily = dailySchedules.findAllBySchedule(schedule);

        //then
        assertThat(findDaily).containsExactlyInAnyOrderElementsOf(List.of(savedDaily));
    }
}