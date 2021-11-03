package netty.c1;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @title: TestFilesWalkFilesTree
 * @Description // 遍历文件（使用Files API）
 * @Author Chen
 * @Date: 2021/11/3 21:33
 * @Version 1.0
 */
public class TestFilesWalkFilesTree {

    public static void main(String[] args) throws IOException {

        final AtomicInteger jarCount = new AtomicInteger();

        Files.walkFileTree(Paths.get("E:\\software\\developer\\jdk8"), new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                if (file.toString().endsWith(".jar")) {
                    System.out.println(file);
                    jarCount.getAndIncrement();
                }
                return super.visitFile(file, attrs);
            }
        });

        System.out.println("jarCount: " + jarCount);

    }

    /**
     * 测试 Files 提供的遍历 API
     * @throws IOException
     */
    private static void test1() throws IOException {
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();

        Files.walkFileTree(Paths.get("E:\\software\\developer\\jdk8"), new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // 遍历目录之前
                System.out.println("===> " + dir);
                dirCount.getAndIncrement();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // 访问文件
                System.out.println(file);
                fileCount.getAndIncrement();
                return super.visitFile(file, attrs);
            }
        });

        System.out.println("dirCount: " + dirCount);
        System.out.println("fileCount: " + fileCount);
    }

}
