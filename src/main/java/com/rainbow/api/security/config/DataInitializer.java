package com.rainbow.api.security.config;

import com.rainbow.api.entity.Activity;
import com.rainbow.api.entity.ActivityType;
import com.rainbow.api.entity.Course;
import com.rainbow.api.entity.CourseLevel;
import com.rainbow.api.entity.Ebook;
import com.rainbow.api.entity.Lesson;
import com.rainbow.api.entity.Permission;
import com.rainbow.api.entity.QuizOption;
import com.rainbow.api.entity.QuizQuestion;
import com.rainbow.api.entity.Role;
import com.rainbow.api.entity.StudentProgress;
import com.rainbow.api.entity.StudentProgressStatus;
import com.rainbow.api.entity.Subscription;
import com.rainbow.api.entity.SubscriptionStatus;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.entity.WordSearchWord;
import com.rainbow.api.repository.ICourseRepository;
import com.rainbow.api.repository.IEbookRepository;
import com.rainbow.api.repository.IEbookPurchaseRepository;
import com.rainbow.api.repository.IActivityRepository;
import com.rainbow.api.repository.ILessonRepository;
import com.rainbow.api.repository.IPermissionRepository;
import com.rainbow.api.repository.IQuizOptionRepository;
import com.rainbow.api.repository.IQuizQuestionRepository;
import com.rainbow.api.repository.IRoleRepository;
import com.rainbow.api.repository.ISubscriptionRepository;
import com.rainbow.api.repository.IStudentProgressRepository;
import com.rainbow.api.repository.IUserRepository;
import com.rainbow.api.repository.IWordSearchWordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Transactional
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;
    private final IUserRepository userRepository;
    private final ICourseRepository courseRepository;
    private final ILessonRepository lessonRepository;
    private final IActivityRepository activityRepository;
    private final IWordSearchWordRepository wordSearchWordRepository;
    private final IQuizQuestionRepository quizQuestionRepository;
    private final IQuizOptionRepository quizOptionRepository;
    private final IEbookRepository ebookRepository;
    private final ISubscriptionRepository subscriptionRepository;
    private final IStudentProgressRepository studentProgressRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            IRoleRepository roleRepository,
            IPermissionRepository permissionRepository,
            IUserRepository userRepository,
            ICourseRepository courseRepository,
            ILessonRepository lessonRepository,
            IActivityRepository activityRepository,
            IWordSearchWordRepository wordSearchWordRepository,
            IQuizQuestionRepository quizQuestionRepository,
            IQuizOptionRepository quizOptionRepository,
            IEbookRepository ebookRepository,
            ISubscriptionRepository subscriptionRepository,
            IStudentProgressRepository studentProgressRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.activityRepository = activityRepository;
        this.wordSearchWordRepository = wordSearchWordRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizOptionRepository = quizOptionRepository;
        this.ebookRepository = ebookRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.studentProgressRepository = studentProgressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Permission read = getOrCreatePermission("READ");
        Permission create = getOrCreatePermission("CREATE");
        Permission update = getOrCreatePermission("UPDATE");
        Permission delete = getOrCreatePermission("DELETE");

        Role teacherRole = getOrCreateRole("TEACHER", Set.of(read, create, update, delete));
        Role studentRole = getOrCreateRole("STUDENT", Set.of(read));

        getOrCreateUser("profesora@rainbow.com", "Admin1234!", "Profesora Rainbow", teacherRole);
        getOrCreateUser("alumno@rainbow.com", "Alumno1234!", "Alumno Demo", studentRole);

        if (courseRepository.count() == 0) {
            seedAcademicDemoData();
        }

        log.info("DataInitializer: datos iniciales verificados/creados correctamente.");
    }

    private void seedAcademicDemoData() {
        Course beginner = courseRepository.save(Course.builder()
                .title("Inglés Principiante")
                .description("Curso base para empezar a hablar inglés con confianza.")
                .level(CourseLevel.BEGINNER)
                .monthlyPrice(BigDecimal.valueOf(15000))
                .active(true)
                .build());

        courseRepository.save(Course.builder()
                .title("Inglés Intermedio")
                .description("Curso para mejorar fluidez y uso práctico del idioma.")
                .level(CourseLevel.INTERMEDIATE)
                .monthlyPrice(BigDecimal.valueOf(20000))
                .active(true)
                .build());

        courseRepository.save(Course.builder()
                .title("Inglés Avanzado")
                .description("Curso avanzado para precisión, speaking y escenarios reales.")
                .level(CourseLevel.ADVANCED)
                .monthlyPrice(BigDecimal.valueOf(25000))
                .active(true)
                .build());

        Lesson lesson1 = lessonRepository.save(Lesson.builder()
                .course(beginner)
                .orderIndex(1)
                .title("Saludos y presentaciones")
                .videoUrl("https://example.com/videos/lesson-1.mp4")
                .description("Aprendé vocabulario inicial y expresiones básicas para presentarte.")
                .build());

        Activity wordActivity = activityRepository.save(Activity.builder()
                .lesson(lesson1)
                .type(ActivityType.WORD_SEARCH)
                .title("Sopa de letras de saludos")
                .minPassScore(70)
                .build());

        List<String> words = List.of("HELLO", "WORLD", "TEACHER", "ENGLISH", "BOOK");
        for (String word : words) {
            wordSearchWordRepository.save(WordSearchWord.builder().activity(wordActivity).word(word).build());
        }

        Lesson lesson2 = lessonRepository.save(Lesson.builder()
                .course(beginner)
                .orderIndex(2)
                .title("Vocabulario básico")
                .videoUrl("https://example.com/videos/lesson-2.mp4")
                .description("Repasá frases simples y vocabulario cotidiano.")
                .build());

        Activity quizActivity = activityRepository.save(Activity.builder()
                .lesson(lesson2)
                .type(ActivityType.QUIZ)
                .title("Quiz de vocabulario")
                .minPassScore(70)
                .build());

        QuizQuestion question1 = quizQuestionRepository.save(QuizQuestion.builder()
                .activity(quizActivity)
                .questionText("¿Cómo se dice 'hola' en inglés?")
                .orderIndex(1)
                .build());

        quizOptionRepository.save(QuizOption.builder().question(question1).optionText("Hello").isCorrect(true).build());
        quizOptionRepository.save(QuizOption.builder().question(question1).optionText("Goodbye").isCorrect(false).build());
        quizOptionRepository.save(QuizOption.builder().question(question1).optionText("Book").isCorrect(false).build());

        QuizQuestion question2 = quizQuestionRepository.save(QuizQuestion.builder()
                .activity(quizActivity)
                .questionText("¿Qué significa 'teacher'?")
                .orderIndex(2)
                .build());

        quizOptionRepository.save(QuizOption.builder().question(question2).optionText("Profesor/a").isCorrect(true).build());
        quizOptionRepository.save(QuizOption.builder().question(question2).optionText("Mesa").isCorrect(false).build());
        quizOptionRepository.save(QuizOption.builder().question(question2).optionText("Libro").isCorrect(false).build());

        Ebook ebook = ebookRepository.save(Ebook.builder()
                .title("Guía de Phrasal Verbs")
                .description("Material complementario con phrasal verbs para practicar.")
                .price(BigDecimal.valueOf(8500))
                .filePath("/files/guia-phrasal-verbs.pdf")
                .coverUrl("https://example.com/covers/phrasal-verbs.jpg")
                .build());

        UserSec student = userRepository.findByUsername("alumno@rainbow.com")
                .orElseThrow(() -> new IllegalStateException("Usuario alumno no encontrado para la semilla"));

        subscriptionRepository.save(Subscription.builder()
                .student(student)
                .course(beginner)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .amountPaid(BigDecimal.valueOf(15000))
                .build());

        studentProgressRepository.save(StudentProgress.builder()
                .student(student)
                .lesson(lesson1)
                .status(StudentProgressStatus.UNLOCKED)
                .score(0)
                .build());

        studentProgressRepository.save(StudentProgress.builder()
                .student(student)
                .lesson(lesson2)
                .status(StudentProgressStatus.LOCKED)
                .score(0)
                .build());

        log.info("DataInitializer: cursos, lecciones, actividades, ebook y suscripción de demo creados.");
    }

    private Permission getOrCreatePermission(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName)
                .orElseGet(() -> permissionRepository.save(new Permission(null, permissionName)));
    }

    private Role getOrCreateRole(String roleName, Set<Permission> permissions) {
        Role role = roleRepository.findByRole(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRole(roleName);
                    return newRole;
                });

        role.setPermissionsList(new HashSet<>(permissions));
        return roleRepository.save(role);
    }

    private UserSec getOrCreateUser(String username, String rawPassword, String fullName, Role role) {
        UserSec user = userRepository.findByUsername(username)
                .orElseGet(UserSec::new);

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFullName(fullName);
        user.setGraduated(false);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setRolesList(new HashSet<>(Set.of(role)));

        return userRepository.save(user);
    }
}
