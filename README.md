# Quy định Commit Message

## Cấu trúc cơ bản

```
<type>(<scope>): subject
- body
footer
```

## Các thành phần

### Type (bắt buộc)

- **feat**: Tính năng mới
- **fix**: Sửa lỗi
- **docs**: Thay đổi tài liệu
- **style**: Thay đổi không ảnh hưởng đến code (format, dấu cách, v.v.)
- **refactor**: Tái cấu trúc code không thay đổi chức năng
- **perf**: Cải thiện hiệu suất
- **test**: Thêm hoặc sửa test
- **chore**: Thay đổi công cụ build, thư viện, v.v.
- **ci**: Thay đổi CI configuration

### Scope (tùy chọn)

Phạm vi của thay đổi (module, component, file, v.v.)
Ví dụ: `auth`, `router`, `ui`, `api`

### Subject (bắt buộc)

- Mô tả ngắn gọn về thay đổi
- Viết ở thì hiện tại (add, not added)
- Không viết hoa chữ đầu tiên
- Không có dấu chấm ở cuối

### Body (tùy chọn)

- Mô tả chi tiết hơn về thay đổi
- Giải thích lý do và cách thực hiện
- Có thể chia thành nhiều dòng

### Footer (tùy chọn)

- Tham chiếu đến issues, PRs
- Đánh dấu breaking changes

## Ví dụ

```
fix(api): handle network timeout errors
- Implement retry mechanism for API calls to handle unstable connections
Fixes #456
```

## Lưu ý

- Giữ subject dưới 50 ký tự
- Sử dụng body để giải thích chi tiết khi cần
- Luôn tham chiếu đến issues liên quan
- Đánh dấu rõ

# Quy định Git Branch

## Cấu trúc tên nhánh

```
<type>/<description>
```

## Các loại nhánh (type)

- **feature**: Phát triển tính năng mới
- **bugfix**: Sửa lỗi
- **hotfix**: Sửa lỗi khẩn cấp trên production
- **release**: Chuẩn bị phát hành phiên bản mới
- **refactor**: Tái cấu trúc code
- **docs**: Cập nhật tài liệu
- **test**: Thêm hoặc sửa test

## Mô tả (description)

- Sử dụng chữ thường
- Sử dụng dấu gạch ngang (-) để ngăn cách các từ
- Ngắn gọn nhưng mô tả rõ nội dung

## Ví dụ

```
feature/user-authentication
feature/123-implement-login
bugfix/api-timeout
hotfix/critical-security-issue
```

## Quy trình làm việc

1. **Tạo nhánh mới từ develop**:

```
   git checkout develop
   git pull
   git checkout -b feature/new-feature
```

2. **Commit thay đổi theo quy định commit message**
3. **Push nhánh lên remote**:

```
  git push -u origin feature/new-feature
```

4. **Tạo Pull Request vào develop**
5. **Sau khi review và approve, merge vào develop**

## Quy trình merge vào main

1. **Tạo nhánh release từ develop khi sẵn sàng phát hành**:

```
  git checkout develop
  git pull
  git checkout -b release/v1.0.0
```

2. **Kiểm tra và sửa lỗi cuối cùng trên nhánh release**:

   - Chỉ sửa lỗi, không thêm tính năng mới
   - Commit theo quy định

3. **Merge nhánh release vào cả main và develop**:

## Lưu ý

- Nhánh `main` luôn chứa code sẵn sàng cho production
- Nhánh `develop` là nhánh phát triển chính
- Chỉ merge vào `main` từ nhánh `release` hoặc `hotfix`
- Luôn tạo tag version khi merge vào `main`

# Quản lý phiên bản trong Flutter

## Cấu trúc phiên bản

### Trong Flutter, phiên bản được định nghĩa trong file `pubspec.yaml` theo định dạng:

```
  version: x.y.z+b
```

Trong đó:

- **x**: Major version - Thay đổi khi có các thay đổi lớn, không tương thích ngược
- **y**: Minor version - Thay đổi khi thêm tính năng mới nhưng vẫn tương thích ngược
- **z**: Patch version - Thay đổi khi sửa lỗi, không thêm tính năng mới
- **b**: Build number - Tăng dần với mỗi lần build, đặc biệt quan trọng cho các app store

## Quy trình tăng phiên bản

### Khi nào tăng phiên bản

1. **Major version (x)**: Khi có thay đổi API hoặc kiến trúc lớn, không tương thích với phiên bản trước
2. **Minor version (y)**: Khi thêm tính năng mới nhưng vẫn đảm bảo tương thích ngược
3. **Patch version (z)**: Khi sửa lỗi, cải thiện hiệu suất mà không thêm tính năng
4. **Build number (b)**: Tăng với mỗi lần build cho các app store

### Cách thay đổi phiên bản (release)

1. Cập nhật phiên bản trong file `pubspec.yaml`:

```yaml
version: 1.2.3+45 # Ví dụ: phiên bản 1.2.3, build số 45
```

2. Tạo tag git cho phiên bản mới:

```bash
git tag -a v1.2.3 -m "Version 1.2.3"
git push origin v1.2.3
```

