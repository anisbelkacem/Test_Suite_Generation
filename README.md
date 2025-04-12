# Test Suite Generation with MOSA and Random Search

This project implements **automated test case generation** for a Java Class Under Test (CUT) using two search-based algorithms:

- **Random Search**
- **MOSA** (Many-Objective Sorting Algorithm)

The aim is to automatically generate a test suite that **maximizes branch coverage** of the CUT, a fundamental problem in **Search-Based Software Engineering (SBSE)**.

---

## 🚀 Project Overview

While previous assignments focused on minimizing test suites with high coverage (NSGA-II), this project focuses on **generating** a test suite from scratch. Each test case is modeled as a chromosome in a Genetic Algorithm (GA), and **each branch** in the CUT is treated as a **separate optimization goal**.

To deal with the dominance resistance problem caused by many objectives, MOSA introduces:
- A **stateful preference criterion** for better focus
- An **archive** population to retain best solutions

---

## 🧬 Algorithms Implemented

### 🔹 Random Search
A baseline method that randomly generates test cases and keeps the best ones based on branch coverage.

### 🔹 MOSA (Many-Objective Sorting Algorithm)
A modified NSGA-II that:
- Focuses on **yet-uncovered branches**
- Maintains a **preference criterion**
- Stores best solutions in an **archive**
- Uses **subvector dominance** for density estimation

---

## 🧠 Core Concepts

### Chromosome Encoding
Each chromosome is a test case — a sequence of Java `Statements` that:
- Start with a constructor call of the CUT
- Consist of field assignments or method calls
- Use only primitive types, Strings, or `null` for reference types
- Have up to 50 statements per test case

### Branch Distance (Fitness)
Branch distance is used to measure how close a test case comes to executing a branch. Implemented as per standard SBSE metrics, using:
- Predicates like `x < y`, `x == y`, `!(x ≤ y)`
- Normalized branch distances
- Root branches (method entry points) for branchless methods



