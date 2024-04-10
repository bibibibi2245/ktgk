package ktgk;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Thread 1: Đọc dữ liệu từ file student.xml
        executor.execute(new Thread1());

        // Thread 2: Tính tuổi và mã hóa
        executor.execute(new Thread2());

        // Thread 3: Kiểm tra số nguyên tố
        executor.execute(new Thread3());

        executor.shutdown();
    }

    static class Thread1 implements Runnable {
        @Override
        public void run() {
            // Đọc dữ liệu từ file student.xml
            // Mô phỏng đọc file ở đây
            // Sau khi đọc, truyền dữ liệu đọc được cho Thread2
            Student student = new Student("123", "John", "123 Street", new Date());
            Thread2.processStudentData(student);
        }
    }

    static class Thread2 implements Runnable {
        private static Student student;

        public static void processStudentData(Student data) {
            student = data;
        }

        @Override
        public void run() {
            if (student != null) {
                // Tính tuổi và mã hóa
                Date currentDate = new Date();
                long ageInMillis = currentDate.getTime() - student.getDateOfBirth().getTime();
                long ageInYears = ageInMillis / (1000L * 60 * 60 * 24 * 365);

                int encodedAge = encodeAge((int) ageInYears);
                Thread3.processEncodedAge(encodedAge);
            }
        }

        private static int encodeAge(int age) {
            // Mã hóa đơn giản, bạn có thể thay đổi tùy thuộc vào yêu cầu
            return age + 5;
        }
    }

    static class Thread3 implements Runnable {
        private static int encodedAge;

        public static void processEncodedAge(int age) {
            encodedAge = age;
        }

        @Override
        public void run() {
            // Kiểm tra tổng các chữ số có phải là số nguyên tố không
            int sumOfDigits = sumOfDigitsInDateOfBirth();
            boolean isPrime = isPrime(sumOfDigits);

            // Tạo và ghi kết quả vào file kq.xml
            createResultFile(encodedAge, sumOfDigits, isPrime);
        }

        private static int sumOfDigitsInDateOfBirth() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
            String dateOfBirthString = dateFormat.format(Thread2.student.getDateOfBirth());
            int sum = 0;
            for (char c : dateOfBirthString.toCharArray()) {
                if (Character.isDigit(c)) {
                    sum += Character.getNumericValue(c);
                }
            }
            return sum;
        }

        private static boolean isPrime(int number) {
            if (number <= 1) {
                return false;
            }
            for (int i = 2; i <= Math.sqrt(number); i++) {
                if (number % i == 0) {
                    return false;
                }
            }
            return true;
        }

        private static void createResultFile(int encodedAge, int sumOfDigits, boolean isPrime) {
            try {
                File resultFile = new File("kq.xml");
                FileWriter writer = new FileWriter(resultFile);
                writer.write("<Student>\n");
                writer.write("\t<age>" + encodedAge + "</age>\n");
                writer.write("\t<sum>" + sumOfDigits + "</sum>\n");
                writer.write("\t<isDigit>" + isPrime + "</isDigit>\n");
                writer.write("</Student>");
                writer.close();
                System.out.println("Kết quả đã được ghi vào file kq.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class Student {
        private String id;
        private String name;
        private String address;
        private Date dateOfBirth;

        public Student(String id, String name, String address, Date dateOfBirth) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.dateOfBirth = dateOfBirth;
        }

        public Date getDateOfBirth() {
            return dateOfBirth;
        }
    }
}
