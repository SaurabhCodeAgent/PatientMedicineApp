# MedsHandy Application Requirements Document

## 1. Overview
MedsHandy is a comprehensive healthcare management application designed to help patients and caregivers manage medications, health records, and medical appointments efficiently.

## 2. Core Features

### 2.1 Patient Management
- Store detailed patient information including:
  - First Name and Last Name
  - Age
  - Gender
  - Contact Information with Country Code
  - Detailed Address (Street Address, City, State, Postal Code, Country)
- View and edit patient details
- Multiple patient support

### 2.2 Medicine Management
#### 2.2.1 Medicine Inventory
- Add new medicines to inventory
- Track medicine quantity
- Automatic stock updates when medicines are taken
- Low stock alerts (when 3 or fewer doses remain)

#### 2.2.2 Daily Medicine Tracking
- Mark medicines as taken/not taken
- Track medicine consumption history
- Automatic stock reduction when medicine is marked as taken
- Stock restoration when medicine is unmarked as taken

#### 2.2.3 Medicine Reminders
- Set reminders for medicine intake
- Customizable reminder times
- Patient-specific medicine schedules

### 2.3 Health Tracking
- Record and monitor vital health metrics:
  - Weight
  - Blood Pressure (Systolic and Diastolic)
  - Blood Sugar Levels
- Track health metrics over time
- Add notes to health records
- Date-wise health record maintenance
- Edit and delete health records

### 2.4 Document Management
#### 2.4.1 Prescription Management
- Upload prescriptions
- View prescription history
- Organize prescriptions by patient
- Track prescription dates

#### 2.4.2 Medical Bills
- Upload medical bills
- Track medical expenses
- Organize bills by patient
- Maintain billing history

### 2.5 Doctor Appointments
- Schedule doctor appointments
- Store doctor information:
  - Name
  - Specialty
- Set appointment reminders
- Track appointment history
- Add appointment notes

## 3. User Interface

### 3.1 Main Dashboard
- Welcome message with patient name
- Grid layout of main features
- Modern card-based UI design

### 3.2 Color Scheme
- Primary Colors: Light Green (#8BC34A)
- Secondary Colors: White
- Accent Colors: Green variations
- Text Colors:
  - Primary Text: Dark Green (#2E7D32)
  - Secondary Text: Light Green (#689F38)

### 3.3 Navigation
- Bottom navigation for main sections
- Back button functionality
- Hierarchical navigation structure

## 4. Database Structure

### 4.1 Core Tables
- Patients
- Medicines
- Health Records
- Prescriptions
- Bills
- Appointments
- Medicine Trackers

### 4.2 Data Relationships
- One-to-Many: Patient to Medicines
- One-to-Many: Patient to Health Records
- One-to-Many: Patient to Prescriptions
- One-to-Many: Patient to Bills
- One-to-Many: Patient to Appointments

## 5. Technical Implementation

### 5.1 Architecture
- Android Native Application
- Room Database for local storage
- MVVM Architecture Pattern

### 5.2 Libraries Used
- AndroidX Components
- Room Persistence Library
- Material Design Components
- RecyclerView for list displays
- CardView for modern UI elements

### 5.3 Data Migration
- Database version management
- Fallback migration strategy for schema changes

## 6. Security Features
- Local data storage
- No sensitive data transmission
- Private file storage for documents

## 7. Error Handling
- Input validation
- Graceful error recovery
- User-friendly error messages
- Database transaction management

## 8. Future Enhancements
Consider implementing:
- Cloud backup
- Multiple user profiles
- Export health reports
- Medicine reorder reminders
- Integration with pharmacies
- Telemedicine features

## 9. Dependencies
- Android SDK
- Room Database
- Material Design Components
- AndroidX Libraries

---

_Note: This document reflects the current implementation as of September 2025 and serves as a reference for both users and developers._
